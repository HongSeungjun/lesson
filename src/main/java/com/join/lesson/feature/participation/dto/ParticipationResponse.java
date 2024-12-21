package com.join.lesson.feature.participation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
public class ParticipationResponse {

    private Long participationId;

    private int remainingCount;

    private String createDate;

    private Long userId;

    private String loginId;

    private String name;

    private String profileURL;

    @Builder
    public ParticipationResponse(Long participationId, int remainingCount, LocalDateTime createDate, Long userId, String loginId, String name, String profileURL) {
        this.participationId = participationId;
        this.remainingCount = remainingCount;
        this.createDate = createDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        this.userId = userId;
        this.loginId = loginId;
        this.name = name;
        this.profileURL = profileURL;
    }
}
