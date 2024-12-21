package com.join.lesson.core.common.util;

import com.join.lesson.core.domain.User;
import com.join.lesson.core.entity.web.MemberDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {

    public static String getLoginId() {
        return ((MemberDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
    }

    public static User getUser() {
        return ((MemberDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUser();
    }

    public static Long getUserId() {
        return ((MemberDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUser().getId();
    }

    public static Boolean isPro() {
        if(((MemberDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUser().getRole().equals("ROLE_MEMBER"))
            return false;
        return true;
    }
}
