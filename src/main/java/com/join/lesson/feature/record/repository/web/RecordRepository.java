package com.join.lesson.feature.record.repository.web;

import com.join.lesson.core.entity.web.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {

    Optional<RecordEntity> findByScheduleId(Long scheduleId);
}
