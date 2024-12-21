package com.join.lesson.feature.lesson;

import com.join.lesson.core.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lesson")
@Controller
public class LessonController {

    private final LessonUseCase lessonUseCase;

    @GetMapping
    public ModelAndView Lessons() {

        if (SecurityUtils.isPro()) {
            ModelAndView modelAndView = new ModelAndView("/pro/lessons");
            modelAndView.addObject("lessons", lessonUseCase.getLesson(SecurityUtils.getUserId()));
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("/lesson");
        modelAndView.addObject("lessons", lessonUseCase.getLesson(SecurityUtils.getUserId()));

        return modelAndView;
    }


}
