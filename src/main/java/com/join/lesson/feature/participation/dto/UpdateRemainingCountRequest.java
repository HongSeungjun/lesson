package com.join.lesson.feature.participation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class UpdateRemainingCountRequest {

    @NotNull
    private Long participationId;

    @NotNull
    private int count;

    @Builder
    public UpdateRemainingCountRequest(Long participationId, int count) {
        this.participationId = participationId;
        this.count = count;
    }
}
