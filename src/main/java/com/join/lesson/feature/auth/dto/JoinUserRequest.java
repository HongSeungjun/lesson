package com.join.lesson.feature.auth.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class JoinUserRequest {

     private String loginId;

     private String loginPw;

     private String name;

     private String tel;

}
