package com.mk.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {

    private int status;
    private String message;
    private long timestamp;
}
