package io.petproject.estate.booking.platform.profile.rest.advice;

import io.petproject.estate.booking.platform.profile.exception.UserProfileAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UserProfileAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handle(UserProfileAlreadyExistsException e) {
        log.info("IN - handleUserProfileAlreadyExistsException: {}", e.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "User profile already exists"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

}
