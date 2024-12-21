package com.join.lesson.feature.participation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetParticipatedLessonResponse {

    private Long lessonId;
    private String lessonName;
    private int lessonType;
    private Long proId;
    private String proName;
    private String proTel;
    private String proProfile;
    private int remainingCount;

    @Builder
    public GetParticipatedLessonResponse(Long lessonId, String lessonName, int lessonType, Long proId, String proName, String proTel, String proProfile, int remainingCount) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.proId = proId;
        this.proName = proName;
        this.proTel = proTel;
        this.proProfile = proProfile;
        this.remainingCount = remainingCount;
    }
}
