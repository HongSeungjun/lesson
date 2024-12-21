package com.join.lesson.feature.reservation.repository.web;

import com.join.lesson.core.entity.web.ReservationChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationChangeRepository extends JpaRepository<ReservationChangeEntity, Long> {
}
