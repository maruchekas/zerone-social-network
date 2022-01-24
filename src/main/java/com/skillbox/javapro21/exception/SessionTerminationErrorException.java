package com.skillbox.javapro21.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SessionTerminationErrorException extends CustomException {
    public SessionTerminationErrorException(String message) {
        super(message);
    }
}
