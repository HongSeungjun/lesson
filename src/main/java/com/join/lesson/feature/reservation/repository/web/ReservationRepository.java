package com.join.lesson.feature.reservation.repository.web;

import com.join.lesson.core.entity.web.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    boolean existsByUserIdAndScheduleId(Long memberId, Long scheduleId);

}
