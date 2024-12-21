package com.join.lesson.core.entity.web;

import com.join.lesson.core.common.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "plans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlanEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Column(name = "content", length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @Builder
    private PlanEntity(String content, UserEntity user, ScheduleEntity schedule) {
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }
}
