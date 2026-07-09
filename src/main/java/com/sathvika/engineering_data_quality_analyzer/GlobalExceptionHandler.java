package com.sathvika.engineering_data_quality_analyzer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReportNotFoundException(ReportNotFoundException ex)
    {

        return new ErrorResponse(
                java.time.LocalDateTime.now().toString(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex)
    {
        //ex.getBindingResult()=Gets all validation failures.
        //getFieldError()=Gets the first invalid field (eg) fileName
        //getDefaultMessage()= Gets the message from: @NotBlank(message="File cannot be empty")
        //so  message = "File cannot be empty";
        String message=ex.getBindingResult().getFieldError().getDefaultMessage();

        return new ErrorResponse(
                java.time.LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }


/*
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAllExceptions(Exception ex)
    {
        return ex.getClass().getName()+ " : "+ ex.getMessage();
    }
    */

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(RuntimeException ex)
    {
        return new ErrorResponse(java.time.LocalDateTime.now().toString(),
                                 HttpStatus.BAD_REQUEST.value(),
                                 ex.getMessage());

    }


}
