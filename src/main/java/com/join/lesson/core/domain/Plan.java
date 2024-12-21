package com.join.lesson.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Plan {
    private Long id;

    private String content;

    private User user;

    private Schedule schedule;

    @Builder
    public Plan(Long id, String content, User user, Schedule schedule) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }
}
