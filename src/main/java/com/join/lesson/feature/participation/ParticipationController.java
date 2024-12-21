package com.join.lesson.feature.participation;

import com.join.lesson.core.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/participation")
@Controller
public class ParticipationController {

    private final ParticipationUseCase participationUseCase;

    @GetMapping("/{lessonId}")
    public ModelAndView LessonParticipaion(@PathVariable("lessonId") Long lessonId) {

        if (!SecurityUtils.isPro()) {
            ModelAndView modelAndView = new ModelAndView("/index");
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("/pro/lesson-participation");
        modelAndView.addObject("participations", participationUseCase.getLessonParticipaion(lessonId));
        modelAndView.addObject("lessonId", lessonId);

        return modelAndView;
    }

    @GetMapping
    public ModelAndView ParticipatedLesson() {

        ModelAndView modelAndView = new ModelAndView("/member/lessons");
        modelAndView.addObject("lessons", participationUseCase.getParticipatedLessonForMember(SecurityUtils.getUserId(), null));

        return modelAndView;
    }

}