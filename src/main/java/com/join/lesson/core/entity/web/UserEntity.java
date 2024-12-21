package com.join.lesson.core.entity.web;

import com.join.lesson.feature.auth.dto.JoinUserRequest;
import com.join.lesson.core.common.TimeBaseEntity;
import com.join.lesson.feature.user.dto.UpdateUserRequset;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login_id", unique = true, nullable = false, length = 20)
    private String loginId;

    @Column(name = "login_pw", nullable = false, length = 60)
    private String loginPw;

    @Column(name = "name", nullable = false, updatable = false, length = 20)
    private String name;

    @Column(name = "tel", unique = true, nullable = false, length = 13)
    private String tel;

    @Column(name = "profile_url", length = 500)
    private String profileUrl;

    @Column(name = "comment", length = 100)
    private String comment;

    @Column(name = "role", nullable = false)
    private String role; // ROLE_MEMBER, ROLE_PRO

    @Column(name = "active", nullable = false)
    private int active;

    @Builder
    public UserEntity(String loginId, String loginPw, String name, String tel, String profileUrl, String comment, String role, int active) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.tel = tel;
        this.profileUrl = profileUrl;
        this.comment = comment;
        this.role = role;
        this.active = active;
    }

    public static UserEntity toEntity(JoinUserRequest joinUserRequest, String encPassword) {
        return UserEntity.builder()
                .loginId(joinUserRequest.getLoginId())
                .loginPw(encPassword)
                .name(joinUserRequest.getName())
                .tel(joinUserRequest.getTel())
                .role("ROLE_MEMBER")
                .build();
    }

    public void changeInfo(UpdateUserRequset updateUserRequset) {
        this.comment = updateUserRequset.getComment();
        this.tel = updateUserRequset.getTel();
    }
}
