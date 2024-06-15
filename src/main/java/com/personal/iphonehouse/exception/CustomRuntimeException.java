package com.personal.iphonehouse.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomRuntimeException extends RuntimeException {
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private int errorCode;
    private String fieldError;

    public CustomRuntimeException(ErrorCodeProvider errorProvider) {
        super(errorProvider.getErrorMessage());
        this.errorCode = errorProvider.getErrorCode();
        printStackTrace();
    }

    public CustomRuntimeException(ErrorCodeProvider errorProvider, String message) {
        super(errorProvider.getErrorMessage() + " " + message);
        this.errorCode = errorProvider.getErrorCode();
        printStackTrace();
    }

    public CustomRuntimeException(ErrorCodeProvider errorProvider, HttpStatus status) {
        super(errorProvider.getErrorMessage());
        this.errorCode = errorProvider.getErrorCode();
        this.httpStatus = status;
        this.httpStatusCode = httpStatus.value();
        printStackTrace();
    }

    public CustomRuntimeException(ErrorCodeProvider errorProvider, HttpStatus status, String message) {
        super(errorProvider.getErrorMessage() + " " + message);
        this.errorCode = errorProvider.getErrorCode();
        this.httpStatus = status;
        this.httpStatusCode = httpStatus.value();
        printStackTrace();
    }

    public CustomRuntimeException(Exception e, HttpStatus status) {
        super(e.getMessage());
        this.httpStatus = status;
        this.httpStatusCode = httpStatus.value();
        this.setStackTrace(e.getStackTrace());
        e.printStackTrace();
    }
}
