package com.join.lesson.feature.reservation;

import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.core.domain.Reservation;
import com.join.lesson.core.entity.web.*;
import com.join.lesson.feature.reservation.dto.*;
import com.join.lesson.feature.reservation.repository.web.ReservationChangeQueryRepository;
import com.join.lesson.feature.reservation.repository.web.ReservationChangeRepository;
import com.join.lesson.feature.reservation.repository.web.ReservationQueryRepository;
import com.join.lesson.feature.reservation.repository.web.ReservationRepository;
import com.join.lesson.feature.schedule.repository.web.ScheduleRepositroy;
import com.join.lesson.share.calendar.CalendarService;
import com.join.lesson.share.participation.ParticipationService;
import com.join.lesson.share.schedule.ScheduleService;
import com.join.lesson.share.unavailabletime.UnavailableService;
import com.join.lesson.share.user.UserService;
import com.join.lesson.core.entity.web.*;
import com.join.lesson.feature.reservation.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationUsecase {

    private final ReservationRepository reservationRepository;

    private final ReservationChangeRepository reservationChangeRepository;

    private final ReservationQueryRepository reservationQueryRepository;

    private final ReservationChangeQueryRepository reservationChangeQueryRepository;

    private final UserService userService;

    private final ScheduleService scheduleService;

    private final ParticipationService participationService;

    private final ScheduleRepositroy scheduleRepositroy;

    private final CalendarService calendarService;

    private final UnavailableService unavailableService;

    //    @DistributedLock(key = "#scheduleId")
    @Transactional
    public Long registReservation(Long memberId, Long scheduleId) {

        UserEntity member = userService.getUserById(memberId);

        ScheduleEntity schedule = scheduleService.getScheduleById(scheduleId);

        LessonEntity lesson = schedule.getLesson();

        // 같은 시간에 스케줄이 있는지 확인
        if (!reservationQueryRepository.isScheduleAvailable(schedule.getScheduleStart(), schedule.getScheduleEnd(), memberId))
            throw new BaseException(ErrorCode.ALERADY_EXIST_SCHEDULE);

        // 똑같은 스케줄을 예약 했는지 확인
        if (isAlreadyReserved(member.getId(), schedule.getId()))
            throw new BaseException(ErrorCode.ALERADY_EXIST_RESERVATION);

        // 이용권 횟수 확인
        if (isNotRemainCount(member.getId(), schedule.getLesson().getId()))
            throw new BaseException(ErrorCode.NO_COUNT_REMAIN);

        // 스케줄 상태 검사
        if (isClosedSchedule(schedule.getState()))
            throw new BaseException(ErrorCode.SCHEDULE_STATE_CLOSED);

        // 저장
        ReservationEntity reservation = ReservationEntity.builder()
                .lessonId(schedule.getLesson().getId())
                .user(member)
                .schedule(schedule)
                .state(0)
                .build();

        Long reservationId = reservationRepository.save(reservation).getId();

        // 레슨 스케줄 참여자 수 증가 후 상태 변경
        schedule.increaseReservedCount();

        scheduleRepositroy.save(schedule);

        if (!isFreeLesson(lesson))
            // 이용권 차감
            decreaseRemaingCount(member.getId(), lesson.getId());

        return reservationId;
    }


    @Transactional
    public Long registPrivateReservation(RegistPrivateReservationRequest registPrivateReservationRequest, Long userId) {

        UserEntity member = userService.getUserById(userId);

        // 같은 시간에 스케줄이 있는지 확인
        if (!reservationQueryRepository.isScheduleAvailable(registPrivateReservationRequest.getStart(), registPrivateReservationRequest.getEnd(), userId))
            throw new BaseException(ErrorCode.ALERADY_EXIST_SCHEDULE);

        // 스케줄 등록
        ScheduleEntity scheduleEntity = scheduleService.registPrivateSchedule(registPrivateReservationRequest.getLessonId(), registPrivateReservationRequest.getStart(), registPrivateReservationRequest.getEnd());

        // 예약 저장
        ReservationEntity reservation = ReservationEntity.builder()
                .lessonId(scheduleEntity.getLesson().getId())
                .user(member)
                .schedule(scheduleEntity)
                .state(4)
                .build();

        Long reservationId = reservationRepository.save(reservation).getId();

        return reservationId;
    }

    // 회원 용
    @Transactional
    public Long cancelReservation(Reservation reservationDto) {

        ReservationEntity reservation = reservationRepository.findById(reservationDto.getReservationId()).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));

        if (!isAuthorAuthorized(reservationDto.getUser().getId(), reservation.getUser().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        reservation.cancel(reservationDto.getCancelReaon());

        return reservation.getId();
    }



    @Transactional
    public Long approveGroupCancel(Long reservationId, Long proId) {

        ReservationEntity reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));
        ScheduleEntity scheduleEntity = reservation.getSchedule();

        // 자신의 레슨인지 확인
        if (!isAuthorAuthorized(proId, scheduleEntity.getLesson().getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        reservationRepository.delete(reservation);

        // 스케줄 참여자 수 줄이고 상태 변환
        scheduleEntity.decreaseReservedCount();

        // 레슨 24시간 전에 취소했다면 패널티
        if (isPanalty(reservation.getModifiedDate(), scheduleEntity.getScheduleStart())) {
            log.info("레슨 패널티 ");
            return reservation.getId();
        }

        // 무료 레슨은 횟수가 줄거나 늘지 않는다.
        if (isFreeLesson(scheduleEntity.getLesson())) {
            log.info("무료 레슨 ");
            return reservation.getId();
        }

        // 이용권 반환
        participationService.increaseRemainingCount(reservation.getUser().getId(), reservation.getLessonId());

        return reservation.getId();

    }

    @Transactional
    public Long rejectGroupCancel(Long reservationId, Long proId) {

        ReservationEntity reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));
        ScheduleEntity scheduleEntity = reservation.getSchedule();

        // 자신의 레슨인지 확인
        if (!isAuthorAuthorized(proId, scheduleEntity.getLesson().getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        // 취소 상태 변경
        reservation.setReservationToActive();

        return reservation.getId();

    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservation(Long memberId) {

        return reservationQueryRepository.getReservation(memberId);

    }

    // 회원용
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationHistory(Long memberId) {

        return reservationQueryRepository.getReservationHistory(memberId);

    }

    @Transactional(readOnly = true)
    public List<CanceledReservationResponse> getCanceledReservationForPro(Long lessonId) {

        return reservationQueryRepository.getCanceledReservationForPro(lessonId);

    }

    @Transactional(readOnly = true)
    public List<ReservedMembersResponse> getReservedMember(Long scheduleId) {

        return reservationQueryRepository.getReservedMembers(scheduleId);

    }

    public List<ReservationResponse> getPrivateLessonSchedules(Long memberId, Long lessonId) {

        return reservationQueryRepository.getPrivateLessonSchedules(memberId, lessonId);

    }

    @Transactional
    public Long rejectPrivateReserve(Long reservationId, Long proId) {

        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));

        ScheduleEntity scheduleEntity = reservationEntity.getSchedule();

        // 자신의 레슨인지 확인
        if (!isAuthorAuthorized(proId, scheduleEntity.getLesson().getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        // 예약 취소
        reservationRepository.delete(reservationEntity);
        scheduleRepositroy.delete(scheduleEntity);

        return reservationEntity.getId();
    }

    @Transactional
    public Long approvePrivateReserve(Long reservationId, Long proId) {

        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));

        ScheduleEntity scheduleEntity = reservationEntity.getSchedule();

        // 자신의 레슨인지 확인
        if (!isAuthorAuthorized(proId, scheduleEntity.getLesson().getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        LessonEntity lesson = scheduleEntity.getLesson();
        // 예약 승인
        reservationEntity.setReservationToActive();

        // 레슨 스케줄 상태 변경
        scheduleEntity.privateReservaionApprove();

        // 이용권 차감
        if (!isFreeLesson(lesson))
            decreaseRemaingCount(reservationEntity.getUser().getId(), lesson.getId());

        return reservationEntity.getId();
    }


    // 개인 레슨 신청 내역 조회
    public List<ReservationResponse> getPrivateLessonScheduleApplications(Long lessonId) {

        return reservationQueryRepository.getPrivateLessonScheduleApplications(lessonId);

    }

    @Transactional
    public Long chagePrivateScheduleReservation(ChangePrivateScheduleRequest changePrivateScheduleRequest, Long memberId) {

        ReservationEntity reservationEntity = reservationRepository.findById(changePrivateScheduleRequest.getReservationId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));

        ScheduleEntity schedule = reservationEntity.getSchedule();

        // 같은 시간에 스케줄이 있는지 확인
        if (!reservationQueryRepository.isScheduleAvailable(changePrivateScheduleRequest.getRequestedStart(), changePrivateScheduleRequest.getRequestedEnd(), memberId))
            throw new BaseException(ErrorCode.ALERADY_EXIST_SCHEDULE);

        // 변경 가능 여부 확인
        if (calendarService.isExistTime(changePrivateScheduleRequest.getRequestedStart(),
                changePrivateScheduleRequest.getRequestedEnd(),
                changePrivateScheduleRequest.getProId()))
            throw new BaseException(ErrorCode.ALERADY_EXIST_RESERVATION);


        // 레슨 불가 시간 생성
        Long tempUnTimeId = unavailableService.createTemporaryUnavailableTime(changePrivateScheduleRequest.getOriginStart(), changePrivateScheduleRequest.getOriginEnd(), changePrivateScheduleRequest.getProId());

        // 예약 변경 테이블 생성
        ReservationChangeEntity reservationChangeEntity = ReservationChangeEntity.builder()
                .originStart(schedule.getScheduleStart())
                .originEnd(schedule.getScheduleEnd())
                .requestStart(changePrivateScheduleRequest.getRequestedStart())
                .requestEnd(changePrivateScheduleRequest.getRequestedEnd())
                .state(0)
                .reason(changePrivateScheduleRequest.getReason())
                .unavailableId(tempUnTimeId)
                .reservation(reservationEntity)
                .build();
        reservationChangeRepository.save(reservationChangeEntity);

        // 스케줄 시간 변경
        schedule.changeSchedule(changePrivateScheduleRequest.getRequestedStart(), changePrivateScheduleRequest.getRequestedEnd());

        // 예약 상태 변경
        reservationEntity.manageReservationChange();

        return reservationChangeEntity.getId();

    }

    // 개인 레슨 변경 내역 조회
    public List<ReservationChangeResponse> getPrivateLessonScheduleChangeApplications(Long lessonId) {

        return reservationChangeQueryRepository.getPrivateLessonScheduleChangeApplications(lessonId);

    }

    // 개인 레슨 예약 변경 승인
    @Transactional
    public Long approvePrivateScheduleChange(Long changeId, Long proId) {

        ReservationChangeEntity reservationChangeEntity = reservationChangeRepository.findById(changeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));

        ReservationEntity reservationEntity = reservationChangeEntity.getReservation();

        ScheduleEntity scheduleEntity = reservationEntity.getSchedule();

        // 자신의 레슨인지 확인
        if (!isAuthorAuthorized(proId, scheduleEntity.getLesson().getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        // 예약 변경 승인
        reservationChangeEntity.approve();
        reservationEntity.setReservationToActive();
        scheduleEntity.privateReservaionApprove();

        // 임시 불가 시간 삭제
        unavailableService.deleteTemporaryUnavailableTime(reservationChangeEntity.getUnavailableId());

        return reservationEntity.getId();
    }

    // 개인 레슨 예약 변경 거절
    @Transactional
    public Long rejectPrivateScheduleChange(Long changeId, Long proId) {

        ReservationChangeEntity reservationChangeEntity = reservationChangeRepository.findById(changeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_RESERVATION));

        ReservationEntity reservationEntity = reservationChangeEntity.getReservation();

        ScheduleEntity scheduleEntity = reservationEntity.getSchedule();

        // 자신의 레슨인지 확인
        if (!isAuthorAuthorized(proId, scheduleEntity.getLesson().getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        // 예약 변경 승인
        reservationChangeEntity.reject();
        reservationEntity.setReservationToActive();
        scheduleEntity.changeSchedule(reservationChangeEntity.getOriginStart(), reservationChangeEntity.getOriginEnd());
        scheduleEntity.privateReservaionApprove();

        // 임시 불가 시간 삭제
        unavailableService.deleteTemporaryUnavailableTime(reservationChangeEntity.getUnavailableId());

        return reservationEntity.getId();
    }


    private void decreaseRemaingCount(Long memberId, Long lessionId) {

        participationService.decreaseRemainingCount(memberId, lessionId);

    }

    private boolean isAlreadyReserved(Long memberId, Long scheduleId) {

        return reservationQueryRepository.existReserved(memberId, scheduleId);

    }

    private boolean isClosedSchedule(int state) {
        if (state == 1) return true;
        return false;
    }

    private boolean isNotRemainCount(Long memberId, Long lessonId) {
        int cnt = participationService.getRemainingCount(memberId, lessonId);
        if (cnt == 0) return true;
        return false;
    }

    private boolean isAuthorAuthorized(Long userId, Long ownerId) {
        if (userId != ownerId) return false;
        return true;
    }

    private boolean isPanalty(LocalDateTime modifiedDate, LocalDateTime scheduleStart) {

        Duration duration = Duration.between(modifiedDate, scheduleStart);

        return Math.abs(duration.toHours()) < 24;

    }

    private boolean isFreeLesson(LessonEntity lesson) {

        if (lesson.getLessonProperty() == 0) return false;

        return true;
    }
}
