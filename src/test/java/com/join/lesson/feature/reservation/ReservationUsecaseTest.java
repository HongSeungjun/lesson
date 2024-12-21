package com.join.lesson.feature.reservation;

import com.join.lesson.IntegrationTestSupport;
import com.join.lesson.core.entity.web.*;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.core.entity.web.*;
import com.join.lesson.feature.lesson.repository.web.LessonRepository;
import com.join.lesson.feature.participation.repository.web.ParticipationRepository;
import com.join.lesson.feature.reservation.dto.CanceledReservationResponse;
import com.join.lesson.feature.reservation.dto.ReservationResponse;
import com.join.lesson.feature.reservation.repository.web.ReservationRepository;
import com.join.lesson.feature.schedule.repository.web.ScheduleRepositroy;
import com.join.lesson.feature.user.repository.web.UserRepository;
import com.join.lesson.testUser.WithMockCustomAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@Slf4j
class ReservationUsecaseTest extends IntegrationTestSupport {

    @Autowired
    private ReservationUsecase reservationUsecase;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ScheduleRepositroy scheduleRepositroy;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @DisplayName("이미 동일한 스케줄을 예약했다면 중복으로 예약 할 수 없다.")
    @Test
    public void duplicateReservationTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "ROLE_PRO");
        UserEntity member = createUser("1", "1", "01011111111", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30, 0);

        ParticipationEntity participation = createParticipation(member, lesson, 30);

        ReservationEntity reservation = createReservation(member, schedule, lesson.getId());

        //when //then
        assertThatThrownBy(() -> reservationUsecase.registReservation(member.getId(), schedule.getId()))
                .isInstanceOf(BaseException.class)
                .satisfies(e -> {
                    BaseException baseException = (BaseException) e;
                    assertThat(baseException.getErrorCode()).isEqualTo(ErrorCode.ALERADY_EXIST_RESERVATION);
                });
    }

    @Transactional
    @DisplayName("예약시 남은 회원권이 없으면 예외가 발생한다.")
    @Test
    public void checkReamingCountTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "ROLE_PRO");
        UserEntity member = createUser("1", "1", "01011111111", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30, 0);

        ParticipationEntity participation = createParticipation(member, lesson, 0);

        //when //then
        assertThatThrownBy(() -> reservationUsecase.registReservation(member.getId(), schedule.getId()))
                .isInstanceOf(BaseException.class)
                .satisfies(e -> {
                    BaseException baseException = (BaseException) e;
                    assertThat(baseException.getErrorCode()).isEqualTo(ErrorCode.NO_COUNT_REMAIN);
                });
    }

    @Transactional
    @DisplayName("예약시 스케줄이 마감 되었다면 예외가 발생한다.")
    @Test
    public void checkScheduleStateTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "ROLE_PRO");
        UserEntity member = createUser("1", "1", "01011111111", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30, 1);

        ParticipationEntity participation = createParticipation(member, lesson, 30);

        //when //then
        assertThatThrownBy(() -> reservationUsecase.registReservation(member.getId(), schedule.getId()))
                .isInstanceOf(BaseException.class)
                .satisfies(e -> {
                    BaseException baseException = (BaseException) e;
                    assertThat(baseException.getErrorCode()).isEqualTo(ErrorCode.SCHEDULE_STATE_CLOSED);
                });
    }

    @Transactional
    @DisplayName("사용자는 레슨 스케줄을 예약 할 수 있다.")
    @Test
    public void reservationTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "ROLE_PRO");
        UserEntity member = createUser("1", "1", "01011111111", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 1, 0);

        ParticipationEntity participation = createParticipation(member, lesson, 30);

        // when
        Long reservation = reservationUsecase.registReservation(member.getId(), schedule.getId());

        // then
        assertThat(schedule.getReservedCount()).isEqualTo(1);
        assertThat(schedule.getState()).isEqualTo(1);
        assertThat(participation.getRemainingCount()).isEqualTo(29);

    }

    @DisplayName("레슨 여러명이서 순차적으로 예약")
    @Test
    public void sequentialReservationTest() throws Exception {
        //given
        UserEntity pro = createUser("test", "test", "01012001234", "ROLE_PRO");

        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            userList.add(createUser(i + "", i + "", "010" + i, "ROLE_MEMBER"));
        }

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 10, 0);

        for (int i = 0; i < 30; i++) {
            createParticipation(userList.get(i), lesson, 30);
        }

        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();
        // when
        for (int i = 0; i < 30; i++) {
            try {
                reservationUsecase.registReservation(userList.get(i).getId(), schedule.getId());
                successCnt.incrementAndGet();
            } catch (Exception e) {
                failCnt.incrementAndGet();
            }
        }

        System.out.println("sucessCnt = " + successCnt);
        System.out.println("failCnt = " + failCnt);
    }

    @DisplayName("레슨 여러명이서 동시적으로 예약")
    @Test
    public void concurrentReservationTest() throws Exception {
        //given
        UserEntity pro = createUser("pro1", "hong", "01012349994", "ROLE_PRO");
        int count = 30;
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            userList.add(createUser(i + "", i + "", "010" + i, "ROLE_MEMBER"));
        }

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 10, 0);

        for (int i = 0; i < count; i++) {
            createParticipation(userList.get(i), lesson, count);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch latch = new CountDownLatch(count);

        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();

        // when
        for (int i = 0; i < count; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    reservationUsecase.registReservation(userList.get(index).getId(), schedule.getId());
                    successCnt.incrementAndGet();
                } catch (Exception e) {
                    failCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        System.out.println("sucessCnt = " + successCnt);
        System.out.println("failCnt = " + failCnt);
    }


    @Transactional
    @DisplayName("회원은 자신이 예약한 내역을 조회 할 수 있다.")
    @WithMockCustomAccount(loginId = "member", name = "member", tel = "01011111111", role = "ROLE_MEMBER")
    @Test
    public void getReservationTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "ROLE_PRO");
        UserEntity member = createUser("member", "memeber", "01011111111", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule1 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30, 0);
        ScheduleEntity schedule2 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "2회차", 30, 0);
        ScheduleEntity schedule3 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "3회차", 30, 0);


        ParticipationEntity participation = createParticipation(member, lesson, 30);

        ReservationEntity reservation1 = createReservation(member, schedule1, lesson.getId());
        ReservationEntity reservation2 = createReservation(member, schedule2, lesson.getId());
        ReservationEntity reservation3 = createReservation(member, schedule3, lesson.getId());

        //when
        List<ReservationResponse> results = reservationUsecase.getReservation(member.getId());

        //then
        assertThat(results).hasSize(3);
    }

    @Transactional
    @DisplayName("레슨 프로는 해당하는 레슨에 예약 취소 내역을 확인 할 수 있다.")
    @WithMockCustomAccount(loginId = "pro", name = "hong", tel = "01012345678")
    @Test
    public void getCancelReservationTest() throws Exception {
        //given
        UserEntity pro = createUser("pro", "hong", "01012341234", "ROLE_PRO");
        UserEntity member = createUser("member", "memeber", "01011111111", "ROLE_MEMBER");

        LessonEntity lesson = createLesson("초급반", pro, 0);

        ScheduleEntity schedule1 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "1회차", 30, 0);
        ScheduleEntity schedule2 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "2회차", 30, 0);
        ScheduleEntity schedule3 = createSchedule(lesson, LocalDateTime.now(), LocalDateTime.now(), "3회차", 30, 0);


        ParticipationEntity participation = createParticipation(member, lesson, 30);

        ReservationEntity reservation1 = createReservation(member, schedule1, lesson.getId());
        ReservationEntity reservation2 = createReservation(member, schedule2, lesson.getId());
        ReservationEntity reservation3 = createReservation(member, schedule3, lesson.getId());

        String reason = "테스트";
        reservation1.cancel(reason);
        reservation2.cancel(reason);

        //when
        List<CanceledReservationResponse> results = reservationUsecase.getCanceledReservationForPro(lesson.getId());

        //then
        assertThat(results).hasSize(2);
    }


    private UserEntity createUser(String loginId, String name, String tel, String role) {
        return userRepository.save(
                UserEntity.builder()
                        .loginId(loginId)
                        .loginPw("password")
                        .name(name)
                        .tel(tel)
                        .active(0)
                        .role(role)
                        .build()
        );
    }

    private LessonEntity createLesson(String lessonName, UserEntity user, int type) {
        return lessonRepository.save(
                LessonEntity.builder()
                        .lessonName(lessonName)
                        .lessonType(type)
                        .lessonProperty(0)
                        .userEntity(user)
                        .build());
    }

    private ScheduleEntity createSchedule(LessonEntity lesson, LocalDateTime start, LocalDateTime end, String name, int maxCount, int state) {
        return scheduleRepositroy.save(
                ScheduleEntity.builder()
                        .scheduleName(name)
                        .scheduleStart(start)
                        .scheduleEnd(end)
                        .maxCount(maxCount)
                        .lesson(lesson)
                        .state(state)
                        .reservedCount(0)
                        .build()
        );
    }

    private ReservationEntity createReservation(UserEntity member, ScheduleEntity schedule, Long lessionId) {
        return reservationRepository.save(
                ReservationEntity.builder()
                        .lessonId(lessionId)
                        .user(member)
                        .schedule(schedule)
                        .state(0)
                        .build()
        );
    }

    private ParticipationEntity createParticipation(UserEntity user, LessonEntity lesson, int remainingCount) {
        return participationRepository.save(
                ParticipationEntity.builder()
                        .user(user)
                        .lesson(lesson)
                        .remainingCount(remainingCount)
                        .build());
    }
}
