package com.example.API.Exception;


import java.io.IOException;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = Logger.getLogger(ResponseEntityExceptionHandler.class);

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<VndErrors> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
    return error(e, HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()); // 422
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<VndErrors> entityExistsExceptionHandler(final IllegalStateException e) {
    return error(e, HttpStatus.CONFLICT, e.getMessage()); // 409
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<VndErrors> IOExceptionHandler(final IOException e) {
    return error(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 500

  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<VndErrors> NullPointerException(final NullPointerException e) {
    return error(e, HttpStatus.BAD_REQUEST, e.getMessage()); // 400
  }

  private ResponseEntity<VndErrors> error(final Exception exception, final HttpStatus httpStatus, final String logRef) {
    final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
    LOGGER.error(exception.getMessage());
    return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
  }

}
