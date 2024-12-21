package com.join.lesson.feature.schedule;

import com.join.lesson.core.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/schedule")
@Controller
public class ScheduleController {

    private final ScheduleUseCase scheduleUseCase;

    @GetMapping
    public ModelAndView Schedules() {

        if (SecurityUtils.isPro()) {
            ModelAndView modelAndView = new ModelAndView("pro/lesson-schedule");
            modelAndView.addObject("schedules", scheduleUseCase.getScheduleForPro(SecurityUtils.getUserId()));
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("/member/schedules");
        modelAndView.addObject("schedules", scheduleUseCase.getScheduleWithReservationStatus(1L, SecurityUtils.getUserId()));
        return modelAndView;
    }

    @GetMapping("/private")
    public ModelAndView privateSchedules() {
        ModelAndView modelAndView = new ModelAndView("/member/private-schedules");
        return modelAndView;
    }


    @GetMapping("/calendar")
    public ModelAndView ScheduleCalendar() {

        ModelAndView modelAndView = new ModelAndView("/pro/calendar");

        return modelAndView;
    }
}
