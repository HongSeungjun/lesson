package com.join.lesson.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Reservation {

    private Long reservationId;
    private int state;
    private Long lessonId;
    private String cancelReaon;
    private User user;
    private Schedule schedule;

    @Builder
    public Reservation(Long reservationId, int state, Long lessonId, String cancelReaon, User user, Schedule schedule) {
        this.reservationId = reservationId;
        this.state = state;
        this.lessonId = lessonId;
        this.cancelReaon = cancelReaon;
        this.user = user;
        this.schedule = schedule;
    }

}
