package com.join.lesson.core.entity.web;

import com.join.lesson.core.common.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "state", nullable = false)
    private int state;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @Builder
    public ReservationEntity(Long lessonId, int state, String cancelReason, UserEntity user, ScheduleEntity schedule) {
        this.lessonId = lessonId;
        this.state = state;
        this.cancelReason = cancelReason;
        this.user = user;
        this.schedule = schedule;
    }

    public void cancel(String resaon) {
        this.state = 2;
        this.cancelReason = resaon;
    }

    public void approveCancel() {
        this.state = 3;
    }

    public void setReservationToActive() {
        this.state = 0;
    }

    public void manageReservationChange() {
        this.state = 5;
    }

}
