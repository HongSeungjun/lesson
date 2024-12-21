package com.join.lesson.feature.schedule.repository.web;

import com.join.lesson.core.entity.web.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepositroy extends JpaRepository<ScheduleEntity, Long> {

}
