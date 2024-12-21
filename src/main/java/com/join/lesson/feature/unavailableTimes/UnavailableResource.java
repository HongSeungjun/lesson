package com.join.lesson.feature.unavailableTimes;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.unavailableTimes.dto.RegistUnavailalbeTimeRequest;
import com.join.lesson.feature.unavailableTimes.dto.UnavailableTimeResponse;
import com.join.lesson.feature.unavailableTimes.dto.UpdateUnavailableTimeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unavailableTimes")
public class UnavailableResource  {

    private final UnavailableUsecase unavailableUsecase;

    @GetMapping
    public ApiResponse<List<UnavailableTimeResponse>> getUnavailTimes() {

        return ApiResponse.ok(unavailableUsecase.getUnavailableTimes(SecurityUtils.getUserId()));
    }

    @GetMapping("/{proId}")
    public ApiResponse<List<UnavailableTimeResponse>> getUnavailTimesForMember(@PathVariable Long proId) {

        return ApiResponse.ok(unavailableUsecase.getUnavailableTimes(proId));

    }

    @PostMapping
    public ApiResponse<Long> registUnavailTime(@RequestBody RegistUnavailalbeTimeRequest registUnavailalbeTimeRequest) {

        return ApiResponse.ok(unavailableUsecase.registUnavailableTime(registUnavailalbeTimeRequest, SecurityUtils.getUserId()));

    }

    @PatchMapping
    public ApiResponse<Long> updateUnavailTime(@RequestBody UpdateUnavailableTimeRequest updateUnavailableTimeRequest) {

        return ApiResponse.ok(unavailableUsecase.updateUnavailableTime(updateUnavailableTimeRequest, SecurityUtils.getUserId()));

    }

    @DeleteMapping("/{unavailableId}")
    public ApiResponse<Long> deleteUnavailTime(@PathVariable Long unavailableId) {

        return ApiResponse.ok(unavailableUsecase.deleteUnavailableTime(unavailableId, SecurityUtils.getUserId()));

    }

}
