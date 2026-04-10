package com.pethealth.system.dto;

public class ResponseDTO<T> {
    private int code;
    private String message;
    private T data;
    
    public ResponseDTO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200, "success", data);
    }
    
    public static <T> ResponseDTO<T> success() {
        return new ResponseDTO<>(200, "success", null);
    }
    
    public static <T> ResponseDTO<T> error(int code, String message) {
        return new ResponseDTO<>(code, message, null);
    }
    
    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(400, message, null);
    }
    
    public static <T> ResponseDTO<T> unauthorized() {
        return new ResponseDTO<>(401, "unauthorized", null);
    }
    
    public static <T> ResponseDTO<T> forbidden() {
        return new ResponseDTO<>(403, "forbidden", null);
    }
    
    public static <T> ResponseDTO<T> notFound() {
        return new ResponseDTO<>(404, "not found", null);
    }
    
    public static <T> ResponseDTO<T> serverError() {
        return new ResponseDTO<>(500, "server error", null);
    }
}