package com.join.lesson.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    ErrorCode errorCode;
    String message;
}
