package com.join.lesson.feature.unavailableTimes;

import com.join.lesson.core.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/unavailable-time")
@Controller
public class UnavailableController {

    private final UnavailableUsecase unavailableUsecase;

    @GetMapping
    public ModelAndView SchedulesForPro() {

        ModelAndView modelAndView = new ModelAndView("pro/lesson-unavailable");
        modelAndView.addObject("untimes", unavailableUsecase.getUnavailableTimes(SecurityUtils.getUserId()));
        return modelAndView;
    }


}
