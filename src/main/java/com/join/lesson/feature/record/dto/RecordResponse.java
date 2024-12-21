package com.join.lesson.feature.record.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordResponse {

    private String content;

    @Builder
    public RecordResponse(String content) {
        this.content = content;
    }
}
