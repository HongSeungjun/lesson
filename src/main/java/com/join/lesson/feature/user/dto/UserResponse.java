package com.join.lesson.feature.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long userId;

    private String userName;

    private String tel;

    private String comment;

    private String profileUrl;

    @Builder
    public UserResponse(Long userId, String userName, String tel, String comment, String profileUrl) {
        this.userId = userId;
        this.userName = userName;
        this.tel = tel;
        this.comment = comment;
        this.profileUrl = profileUrl;
    }

}
