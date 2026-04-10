package com.pethealth.system.exception;

import com.pethealth.system.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ResponseDTO<?>> handleBusinessException(BusinessException e) {
        ResponseDTO<?> response = ResponseDTO.error(e.getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getCode()));
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<?> handleException(Exception e) {
        e.printStackTrace();
        return ResponseDTO.serverError();
    }
}