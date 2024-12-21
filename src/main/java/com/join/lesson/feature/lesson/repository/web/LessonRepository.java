package com.join.lesson.feature.lesson.repository.web;

import com.join.lesson.core.entity.web.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {

}
