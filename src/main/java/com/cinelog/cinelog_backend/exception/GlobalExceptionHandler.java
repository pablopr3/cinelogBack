//package com.cinelog.cinelog_backend.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ApiErrorResponse> handleCustom(CustomException ex, HttpServletRequest request) {
//        return buildResponse(ex.getStatus(), ex.getMessage(), request.getRequestURI(), ex.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        String mensaje = ex.getBindingResult().getFieldErrors().stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .findFirst()
//                .orElse("Datos inválidos");
//
//        return buildResponse(HttpStatus.BAD_REQUEST, mensaje, request.getRequestURI(), "Validación fallida");
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorResponse> handleOther(Exception ex, HttpServletRequest request) {
//        ex.printStackTrace(); // solo para debug
//        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.", request.getRequestURI(), "Error interno");
//    }
//
//    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path, String error) {
//        ApiErrorResponse response = ApiErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(status.value())
//                .error(error)
//                .message(message)
//                .path(path)
//                .build();
//
//        return ResponseEntity.status(status).body(response);
//    }
//}
