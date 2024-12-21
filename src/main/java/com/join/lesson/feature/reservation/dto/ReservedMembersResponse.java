package com.join.lesson.feature.reservation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservedMembersResponse {

    private Long memberId;

    private String loginId;

    private String memberName;

    private String memberTel;

    @Builder
    public ReservedMembersResponse(Long memberId, String loginId, String memberName, String memberTel) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.memberName = memberName;
        this.memberTel = memberTel;
    }
}
