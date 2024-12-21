package com.join.lesson.core.entity.web;

import com.join.lesson.core.common.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "participation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ParticipationEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private Long id;

    @Column(name = "remaining_count", nullable = false)
    private int remainingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private LessonEntity lesson;

    @Builder
    public ParticipationEntity(int remainingCount, UserEntity user, LessonEntity lesson) {
        this.remainingCount = remainingCount;
        this.user = user;
        this.lesson = lesson;
    }

    public void changeRemainingCount(int count) {
        this.remainingCount = count;
    }
}
