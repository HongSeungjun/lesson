package com.join.lesson.feature.participation;

import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.ParticipationEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.feature.participation.dto.GetParticipatedLessonResponse;
import com.join.lesson.feature.participation.dto.ParticipationResponse;
import com.join.lesson.feature.participation.dto.RegistParticipantRequest;
import com.join.lesson.feature.participation.dto.UpdateRemainingCountRequest;
import com.join.lesson.feature.participation.repository.web.ParticipationQueryRepository;
import com.join.lesson.feature.participation.repository.web.ParticipationRepository;
import com.join.lesson.share.lesson.LessonService;
import com.join.lesson.share.participation.ParticipationService;
import com.join.lesson.share.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipationUseCase implements ParticipationService {

    private final ParticipationRepository participationRepository;

    private final ParticipationQueryRepository participationQueryRepository;

    private final UserService userService;
    private final LessonService lessonService;

    public Long registParticipant(RegistParticipantRequest registParticipantRequest) {

        UserEntity member = userService.getUserByLoginId(registParticipantRequest.getLoginId());

        LessonEntity lesson = lessonService.getLessonById(registParticipantRequest.getLessonId());

        if (participationQueryRepository.exists(member.getId(), lesson.getId()))
            throw new BaseException(ErrorCode.ALREADY_EXIST_MEMBER);

        ParticipationEntity participation = ParticipationEntity.builder()
                .user(member)
                .lesson(lesson)
                .remainingCount(registParticipantRequest.getCount())
                .build();

        return participationRepository.save(participation).getId();
    }

    @Transactional
    public Long updateMemeberRemainingCount(UpdateRemainingCountRequest updateRemainingCountRequest, Long proId) {

        ParticipationEntity participation = participationRepository.findById(updateRemainingCountRequest.getParticipationId()).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_PARTICIPATION));

        if (isAuthorAuthorized(proId, participation.getLesson().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        participation.changeRemainingCount(updateRemainingCountRequest.getCount());

        return participation.getId();
    }

    @Transactional
    public void deleteParticipant(Long participationId, Long proId) {

        ParticipationEntity participation = participationRepository.findById(participationId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_PARTICIPATION));

        if (isAuthorAuthorized(proId, participation.getLesson().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        participationRepository.delete(participation);
    }

    @Transactional(readOnly = true)
    public List<GetParticipatedLessonResponse> getParticipatedLessonForMember(Long memberId, Integer lessonType) {

        return participationQueryRepository.getParticipatedLessonForMember(memberId,lessonType);

    }

    @Transactional(readOnly = true)
    public List<ParticipationResponse> getLessonParticipaion(Long lessonId) {

        return participationQueryRepository.getLessonParticipaion(lessonId);

    }

    @Transactional
    @Override
    public void deleteAllByLessonId(Long lessonId) {

        if (participationRepository.existsById(lessonId))
            participationRepository.deleteAllByLessonId(lessonId);

    }

    @Override
    public int getRemainingCount(Long memberId, Long lessonId) {

        return participationQueryRepository.getRemainingCount(memberId, lessonId);

    }

    @Transactional
    @Override
    public void decreaseRemainingCount(Long memberId, Long lessonId) {

        ParticipationEntity participation = participationRepository.findByUserIdAndLessonId(memberId, lessonId);

        participation.changeRemainingCount(participation.getRemainingCount() - 1);

    }

    @Transactional
    @Override
    public void increaseRemainingCount(Long memberId, Long lessonId) {
        ParticipationEntity participation = participationRepository.findByUserIdAndLessonId(memberId, lessonId);

        participation.changeRemainingCount(participation.getRemainingCount() + 1);
    }

    private boolean isAuthorAuthorized(Long userId, Long ownerId) {

        if (userId != ownerId) return false;

        return true;
    }
}
