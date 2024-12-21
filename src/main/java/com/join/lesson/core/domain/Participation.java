package com.join.lesson.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Participation {

    private Long id;
    private int remainingCount;
    private User user;
    private Lesson lesson;

    @Builder
    public Participation(Long id, int remainingCount, User user, Lesson lesson) {
        this.id = id;
        this.remainingCount = remainingCount;
        this.user = user;
        this.lesson = lesson;
    }
}
