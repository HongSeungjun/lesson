package com.join.lesson.feature.auth;

import com.join.lesson.feature.auth.dto.JoinUserRequest;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Model;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthUsecase authUsecase;

    @GetMapping("/")
    public String homelogin() {
        if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
            return "login";
        return "index";
    }

    @GetMapping("/join")
    public String join(Model model) {
        return "join";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/joinProc")
    public String joinProc(@RequestBody JoinUserRequest joinUserRequest) {
        authUsecase.join(joinUserRequest);
        return "redirect:/";
    }
}
