package com.join.lesson.share.unavailabletime;

import com.join.lesson.feature.unavailableTimes.dto.UnavailableTimeResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface UnavailableService {

    public List<UnavailableTimeResponse> getUnavailableTimes(Long proId);

    Long createTemporaryUnavailableTime(LocalDateTime requestedStart, LocalDateTime requestedEnd,Long proId);

    Long deleteTemporaryUnavailableTime(Long unavailableId);
}
