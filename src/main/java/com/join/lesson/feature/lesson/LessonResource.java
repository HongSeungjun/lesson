package com.join.lesson.feature.lesson;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.lesson.dto.LessonResponse;
import com.join.lesson.feature.lesson.dto.RegistLessonRequest;
import com.join.lesson.feature.lesson.dto.UpdateLessonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lesson")
public class LessonResource {

    private final LessonUseCase lessonUseCase;

    @PostMapping
    public ApiResponse<Long> registLesson(@Validated @RequestBody RegistLessonRequest registLessonRequest) {

        Long lessonId = lessonUseCase.registLesson(registLessonRequest,SecurityUtils.getUserId());

        return ApiResponse.ok(lessonId);

    }

    @PatchMapping
    public ApiResponse<Long> updateLesson(@Validated @RequestBody UpdateLessonRequest updateLessonRequest) {

        Long lessonId = lessonUseCase.updateLesson(updateLessonRequest, SecurityUtils.getUserId());

        return ApiResponse.ok(lessonId);

    }

    @DeleteMapping("/{lessonId}")
    public ApiResponse<Long> deleteLesson(@PathVariable Long lessonId) {

        lessonUseCase.deleteLesson(lessonId, SecurityUtils.getUserId());

        return ApiResponse.ok(lessonId);

    }

    @GetMapping
    public ApiResponse<List<LessonResponse>> getLessonList() {

        return ApiResponse.ok(lessonUseCase.getLesson(SecurityUtils.getUserId()));

    }



}
