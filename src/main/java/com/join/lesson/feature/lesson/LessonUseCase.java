package com.join.lesson.feature.lesson;

import com.join.lesson.core.entity.web.LessonEntity;
import com.join.lesson.core.entity.web.UserEntity;
import com.join.lesson.core.common.exception.BaseException;
import com.join.lesson.core.common.exception.ErrorCode;
import com.join.lesson.feature.lesson.dto.LessonResponse;
import com.join.lesson.feature.lesson.dto.RegistLessonRequest;
import com.join.lesson.feature.lesson.dto.UpdateLessonRequest;
import com.join.lesson.feature.lesson.repository.web.LessonQueryRepository;
import com.join.lesson.feature.lesson.repository.web.LessonRepository;
import com.join.lesson.share.lesson.LessonService;
import com.join.lesson.share.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class LessonUseCase implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonQueryRepository lessonQueryRepository;
    private final UserService userService;
//    private final ParticipationService participationService;

    public Long registLesson(RegistLessonRequest registLessonRequest, Long userId) {

        UserEntity userEntity = userService.getUserById(userId);
        LessonEntity lesson = lessonRepository.save(LessonEntity.toEntity(registLessonRequest, userEntity));

        return lesson.getId();

    }

    @Transactional
    public Long updateLesson(UpdateLessonRequest updateLessonRequest, Long proId) {

        LessonEntity lesson = lessonRepository.findById(updateLessonRequest.getLessonId()).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LESSON));

        if (!isAuthorAuthorized(proId, lesson.getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        lesson.update(updateLessonRequest);

        return lesson.getId();

    }


    @Transactional
    public void deleteLesson(Long lessonId, Long proId) {

//        participationService.deleteAllByLessonId(lessonId);

        LessonEntity lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LESSON));

        if (!isAuthorAuthorized(proId, lesson.getUserEntity().getId()))
            throw new BaseException(ErrorCode.UNAUTHORIZED);

        lessonRepository.delete(lesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getLesson(Long proId) {

        return lessonQueryRepository.getLesson(proId);

    }

    @Override
    public LessonEntity getLessonById(Long lessonId) {

        LessonEntity lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LESSON));

        return lesson;

    }

    private boolean isAuthorAuthorized(Long userId, Long ownerId) {

        if (userId != ownerId) return false;

        return true;
    }
}
