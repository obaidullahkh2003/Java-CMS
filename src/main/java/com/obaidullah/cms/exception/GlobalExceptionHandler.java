package com.obaidullah.cms.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  //handel specific exceptions
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorObject> handelResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
    ErrorObject errorDetails=new ErrorObject(new Date(),exception.getMessage(),webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BlogAPIException.class)
  public ResponseEntity<ErrorObject> handelBlogAPIException(BlogAPIException exception, WebRequest webRequest) {
    ErrorObject errorDetails=new ErrorObject(new Date(),exception.getMessage(),webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedAPIException.class)
  public ResponseEntity<ErrorObject> handleAccessDeniedAPIException(AccessDeniedAPIException exception, WebRequest webRequest) {
    ErrorObject errorDetails = new ErrorObject(new Date(), exception.getMessage(), webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, exception.getStatus());
  }

  //global exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorObject> handelException(Exception exception, WebRequest webRequest) {
    ErrorObject errorDetails=new ErrorObject(new Date(),exception.getMessage(),webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) ->{
      String fieldName = ((FieldError)error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
