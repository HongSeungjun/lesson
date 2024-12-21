package com.join.lesson.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 204 NO_CONTENT */
    /* 400 BAD_REQUEST */
    NO_COUNT_REMAIN(HttpStatus.BAD_REQUEST, "이용권이 없습니다."),
    SCHEDULE_STATE_CLOSED(HttpStatus.BAD_REQUEST, "예약이 마감 되었습니다."),

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    /* 403 FORBIDDEN */
    /* 404 NOT_FOUND */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "등록된 회원이 없습니다.."),
    NOT_FOUND_LESSON(HttpStatus.NOT_FOUND, "등록된 레슨이 없습니다."),
    NOT_FOUND_PARTICIPATION(HttpStatus.NOT_FOUND, "등록된 참여자가 없습니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "등록된 스케줄이 없습니다.."),
    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "등록된 예약이 없습니다."),
    NOT_FOUND_RECORD(HttpStatus.NOT_FOUND, "등록된 기록이 없습니다. "),
    NOT_FOUND_UNAVAILALBE_TIME(HttpStatus.NOT_FOUND, "등록된 타임이 없습니다."),


    /* 409 CONFLICT */
    ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다.."),
    ALERADY_EXIST_RESERVATION(HttpStatus.CONFLICT, "이미 예약 되었습니다."),
    ALERADY_EXIST_SCHEDULE(HttpStatus.CONFLICT, "동시간대 다른 스케줄이 예약되어 있습니다."),

    /* 500 SERVER ERROR */
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 실패");

    private final HttpStatus httpStatus;
    private final String message;

}
