package com.join.lesson.share.calendar;

import java.time.LocalDateTime;

public interface CalendarService {

    Boolean isExistTime(LocalDateTime requestStart, LocalDateTime requestEnd, Long proId);

}
