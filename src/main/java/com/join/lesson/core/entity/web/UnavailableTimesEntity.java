package com.join.lesson.core.entity.web;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Table(name = "unavailable_times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UnavailableTimesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unavailable_id")
    private Long id;

    @Column(name = "day_of_week", nullable = false)
    private int daysOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Builder
    public UnavailableTimesEntity(int daysOfWeek, LocalTime startTime, LocalTime endTime, UserEntity userEntity) {
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userEntity = userEntity;
    }

    public void changeTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.daysOfWeek = startTime.getDayOfWeek().getValue();
        this.startTime = startTime.toLocalTime();
        this.endTime = endTime.toLocalTime();
    }
}
