package com.join.lesson.feature.unavailableTimes.repository.web;

import com.join.lesson.core.entity.web.UnavailableTimesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnavailableRepository extends JpaRepository<UnavailableTimesEntity, Long> {

}
