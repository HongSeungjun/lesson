package com.join.lesson.feature.participation;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.participation.dto.GetParticipatedLessonResponse;
import com.join.lesson.feature.participation.dto.RegistParticipantRequest;
import com.join.lesson.feature.participation.dto.UpdateRemainingCountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participation")
public class ParticipationResource {

    private final ParticipationUseCase participationUseCase;

    @PostMapping
    public ApiResponse<Long> registParticipation(@Validated @RequestBody RegistParticipantRequest registParticipantRequest) {

        Long participationId = participationUseCase.registParticipant(registParticipantRequest);

        return ApiResponse.ok(participationId);
    }

    @PatchMapping
    public ApiResponse<Long> updateParticipation(@Validated @RequestBody UpdateRemainingCountRequest updateRemainingCountRequest) {

        Long participationId = participationUseCase.updateMemeberRemainingCount(updateRemainingCountRequest, SecurityUtils.getUserId());

        return ApiResponse.ok(participationId);
    }

    @DeleteMapping("/{participationId}")
    public ApiResponse<Long> deleteParticipation(@Validated @PathVariable("participationId") Long participationId) {

        participationUseCase.deleteParticipant(participationId, SecurityUtils.getUserId());

        return ApiResponse.ok(participationId);
    }

    @GetMapping
    public ApiResponse<List<GetParticipatedLessonResponse>> getParticipatedLessonForMember(
            @RequestParam(required = false) Integer lessonType) {
        return ApiResponse.ok(participationUseCase.getParticipatedLessonForMember(SecurityUtils.getUserId(), lessonType));
    }

}
