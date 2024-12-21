package com.join.lesson.feature.record.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class UpdateRecordRequest {

    @NotBlank
    private String content;

    @NotNull
    private Long scheduleId;

    @Builder
    public UpdateRecordRequest(String content, Long scheduleId) {
        this.content = content;
        this.scheduleId = scheduleId;
    }
}
