package com.join.lesson.feature.participation.repository.web;


import com.join.lesson.core.entity.web.ParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long> {

    void deleteAllByLessonId(Long lessonId);

    ParticipationEntity findByUserIdAndLessonId(Long memberId, Long lessonId);

    List<ParticipationEntity> findByLessonId(Long lessonId);
}
