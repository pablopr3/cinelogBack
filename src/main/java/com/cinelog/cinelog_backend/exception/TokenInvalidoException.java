package com.cinelog.cinelog_backend.exception;

import org.springframework.http.HttpStatus;

public class TokenInvalidoException extends CustomException {
    public TokenInvalidoException() {
        super("El token es inválido o ya fue usado.", HttpStatus.BAD_REQUEST);
    }
}
