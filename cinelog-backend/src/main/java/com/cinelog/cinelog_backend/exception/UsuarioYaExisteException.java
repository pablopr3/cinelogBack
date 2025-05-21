package com.cinelog.cinelog_backend.exception;

import org.springframework.http.HttpStatus;

public class UsuarioYaExisteException extends CustomException {
    public UsuarioYaExisteException(String mensaje) {
        super(mensaje, HttpStatus.BAD_REQUEST);
    }
}
