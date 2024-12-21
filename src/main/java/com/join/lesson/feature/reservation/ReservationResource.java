package com.join.lesson.feature.reservation;


import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.reservation.dto.*;
import com.join.lesson.feature.reservation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationResource {

    private final ReservationUsecase reservationUsecase;

    @PostMapping("/{scheduleId}")
    public ApiResponse<Long> registReservation(@PathVariable("scheduleId") Long scheduleId) {

        Long participationId = reservationUsecase.registReservation(SecurityUtils.getUser().getId(), scheduleId);

        return ApiResponse.ok(participationId);

    }

    @PostMapping
    public ApiResponse<Long> registPrivateReservation(@RequestBody RegistPrivateReservationRequest registPrivateReservationRequest) {

        Long participationId = reservationUsecase.registPrivateReservation(registPrivateReservationRequest, SecurityUtils.getUserId());

        return ApiResponse.ok(participationId);

    }


    @GetMapping
    public ApiResponse<List<ReservationResponse>> getReservation() {

        return ApiResponse.ok(reservationUsecase.getReservation(SecurityUtils.getUserId()));

    }

    @GetMapping("/{scheduleId}")
    public ApiResponse<List<ReservedMembersResponse>> getReservedMembers(@PathVariable("scheduleId") Long scheduleId) {

        return ApiResponse.ok(reservationUsecase.getReservedMember(scheduleId));

    }

    @GetMapping("/private-schedules/{lessonId}")
    public ApiResponse<List<ReservationResponse>> getPrivateLessonSchedules(@PathVariable Long lessonId) {

        return ApiResponse.ok(reservationUsecase.getPrivateLessonSchedules(SecurityUtils.getUserId(), lessonId));

    }

    @PatchMapping
    public ApiResponse<Long> cancelReservation(@RequestBody CancelReservationRequest cancelReservationRequest) {

        Long resultId = reservationUsecase.cancelReservation(cancelReservationRequest.toDomain(SecurityUtils.getUser()));

        return ApiResponse.ok(resultId);

    }

    @PatchMapping("/{reservationId}/approve-cancellation")
    public ApiResponse<Long> approveCancelReservation(@PathVariable("reservationId") Long reservationId) {

        Long resultId = reservationUsecase.approveGroupCancel(reservationId, SecurityUtils.getUserId());

        return ApiResponse.ok(resultId);

    }

    @PatchMapping("/{reservationId}/reject-cancellation")
    public ApiResponse<Long> rejectCancelReservation(@PathVariable("reservationId") Long reservationId) {

        Long resultId = reservationUsecase.rejectGroupCancel(reservationId, SecurityUtils.getUserId());

        return ApiResponse.ok(resultId);

    }

    @PatchMapping("/private/{reservationId}")
    public ApiResponse<Long> approvePrivateReservation(@PathVariable("reservationId") Long reservationId) {

        Long resultId = reservationUsecase.approvePrivateReserve(reservationId, SecurityUtils.getUserId());

        return ApiResponse.ok(resultId);

    }

    @DeleteMapping("/private/{reservationId}")
    public ApiResponse<Long> rejectPrivateReservation(@PathVariable("reservationId") Long reservationId) {

        Long resultId = reservationUsecase.rejectPrivateReserve(reservationId, SecurityUtils.getUserId());

        return ApiResponse.ok(resultId);

    }

    @GetMapping("/cancelled/{lessonId}")
    public ApiResponse<List<CanceledReservationResponse>> getCanceledReservation(@PathVariable("lessonId") Long lessonId) {

        return ApiResponse.ok(reservationUsecase.getCanceledReservationForPro(lessonId));

    }

    @GetMapping("/private/{lessonId}")
    public ApiResponse<List<ReservationResponse>> getPrivateLessonScheduleApplications(@PathVariable("lessonId") Long lessonId) {

        return ApiResponse.ok(reservationUsecase.getPrivateLessonScheduleApplications(lessonId));

    }

    @GetMapping("/private/request-change/{lessonId}")
    public ApiResponse<List<ReservationChangeResponse>> getPrivateLessonScheduleChangeApplications(@PathVariable("lessonId") Long lessonId) {

        return ApiResponse.ok(reservationUsecase.getPrivateLessonScheduleChangeApplications(lessonId));

    }

    @PatchMapping("/private/request-change")
    public ApiResponse<Long> changePrivateScheduleReservation(@RequestBody ChangePrivateScheduleRequest changePrivateScheduleRequest) {

        return ApiResponse.ok(reservationUsecase.chagePrivateScheduleReservation(changePrivateScheduleRequest, SecurityUtils.getUserId()));

    }

    @PatchMapping("/private/approve-change/{changeId}")
    public ApiResponse<Long> approvePrivateScheduleReservatioChange(@PathVariable Long changeId) {

        return ApiResponse.ok(reservationUsecase.approvePrivateScheduleChange(changeId, SecurityUtils.getUserId()));

    }

    @PatchMapping("/private/reject-change/{changeId}")
    public ApiResponse<Long> rejectPrivateScheduleReservationChange(@PathVariable Long changeId) {

        return ApiResponse.ok(reservationUsecase.rejectPrivateScheduleChange(changeId, SecurityUtils.getUserId()));

    }


}
