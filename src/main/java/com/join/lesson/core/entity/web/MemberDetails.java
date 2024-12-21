package com.join.lesson.core.entity.web;

import com.join.lesson.core.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class MemberDetails implements UserDetails {

    private final UserEntity user;

    public MemberDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority(user.getRole())
        );
    }

    @Override
    public String getPassword() {
        return user.getLoginPw();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    public User getUser() {
        return User.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .loginPw(user.getLoginPw())
                .name(user.getName())
                .tel(user.getTel())
                .profileUrl(user.getProfileUrl())
                .comment(user.getComment())
                .role(user.getRole())
                .active(user.getActive())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
