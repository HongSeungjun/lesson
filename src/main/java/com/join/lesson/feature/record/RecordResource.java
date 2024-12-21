package com.join.lesson.feature.record;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.feature.record.dto.RecordResponse;
import com.join.lesson.feature.record.dto.RegistRecordRequest;
import com.join.lesson.feature.record.dto.UpdateRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordResource {

    private final RecordUsecase recordUsecase;

    @PostMapping
    public ApiResponse<Long> registRecord(@Validated @RequestBody RegistRecordRequest registRecordRequest) {

        return ApiResponse.ok(recordUsecase.registRecord(registRecordRequest));

    }

    @PatchMapping
    public ApiResponse<Long> updateRecord(@Validated @RequestBody UpdateRecordRequest updateRecordRequest) {

        return ApiResponse.ok(recordUsecase.updateRecord(updateRecordRequest));

    }
    @GetMapping("/{scheduleId}")
    public ApiResponse<RecordResponse> getRecord(@Validated @PathVariable("scheduleId") Long scheduleId) {

        return ApiResponse.ok(recordUsecase.getRecord(scheduleId));

    }
}
