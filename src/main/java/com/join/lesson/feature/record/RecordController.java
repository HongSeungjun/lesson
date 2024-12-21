package com.join.lesson.feature.record;


import com.join.lesson.core.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/record")
@Controller
public class RecordController {

    private final RecordUsecase recordUsecase;

    @GetMapping("/{scheduleId}")
    public ModelAndView registRecord(@PathVariable Long scheduleId) {

        if (!SecurityUtils.isPro()) {
            ModelAndView modelAndView = new ModelAndView("/index");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("/pro/lesson-record");
        modelAndView.addObject("scheduleId", scheduleId);
        return modelAndView;
    }

}
