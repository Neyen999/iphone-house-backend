package com.personal.iphonehouse.controllers;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.personal.iphonehouse.exception.CustomRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 7)
public class ControllerErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RuntimeException> handleException(Exception ex) {
        boolean hasCause = ex.getCause() != null;
        Throwable throwable = hasCause ? ex.getCause() : ex;
        HttpStatus status = getHttpStatus(throwable);

        CustomRuntimeException runtimeException = new CustomRuntimeException(ex, status);
        if (hasCause && throwable instanceof MismatchedInputException) {
            MismatchedInputException mismatchedInputException = (MismatchedInputException) ex.getCause();
            if (mismatchedInputException.getPath().size() > 0) {
                runtimeException.setFieldError(mismatchedInputException.getPath().get(0).getDescription());
            }
        }

        return new ResponseEntity<>(runtimeException, status);
    }

    private HttpStatus getHttpStatus(Throwable ex) {
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        } else if (ex instanceof MaxUploadSizeExceededException) {
            return HttpStatus.PAYLOAD_TOO_LARGE;
        } else if (ex instanceof NoHandlerFoundException
                || ex instanceof MismatchedInputException
                || ex instanceof IllegalArgumentException
                || ex instanceof NullPointerException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof BadCredentialsException
                || ex instanceof AuthenticationException
                || ex instanceof InsufficientAuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof AccessDeniedException
                || ex instanceof DisabledException
                || ex instanceof AccountExpiredException
                || ex instanceof CredentialsExpiredException
                || ex instanceof LockedException) {
            return HttpStatus.FORBIDDEN;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
