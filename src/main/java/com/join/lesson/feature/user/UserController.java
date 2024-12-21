package com.join.lesson.feature.user;

import com.join.lesson.core.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {


    @GetMapping
    public ModelAndView mypage() {

        if (SecurityUtils.isPro()) {
            ModelAndView modelAndView = new ModelAndView("pro/mypage");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("/member/mypage");
        return modelAndView;
    }
}
