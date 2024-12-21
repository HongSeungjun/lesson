package com.join.lesson.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Record {

    private Long id;
    private String content;
    private String feedback;
    private User user;
    private Schedule schedule;

    @Builder
    public Record(Long id, String content, String feedback, User user, Schedule schedule) {
        this.id = id;
        this.content = content;
        this.feedback = feedback;
        this.user = user;
        this.schedule = schedule;
    }
}
