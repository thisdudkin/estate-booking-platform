package io.petproject.estate.booking.platform.identity.rest.advice;

import io.petproject.estate.booking.platform.identity.exception.IdentityProviderConflictException;
import io.petproject.estate.booking.platform.identity.exception.IdentityProviderException;
import io.petproject.estate.booking.platform.identity.exception.ProfileServiceIntegrationException;
import io.petproject.estate.booking.platform.identity.exception.ProfileServiceUnavailableException;
import io.petproject.estate.booking.platform.identity.exception.RegistrationConflictException;
import io.petproject.estate.booking.platform.identity.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.net.URI;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({RegistrationConflictException.class, IdentityProviderConflictException.class})
    ResponseEntity<ProblemDetail> handleConflict(RuntimeException exception, HttpServletRequest request) {
        return response(
            HttpStatus.CONFLICT,
            "Registration conflict",
            exception.getMessage(),
            "registration_conflict",
            request
        );
    }

    @ExceptionHandler({
        ProfileServiceUnavailableException.class,
        IdentityProviderException.class,
        RegistrationFailedException.class
    })
    ResponseEntity<ProblemDetail> handleDependencyUnavailable(
        RuntimeException exception,
        HttpServletRequest request
    ) {
        log.warn("Registration dependency failure: {}", exception.getMessage(), exception);
        return response(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Registration temporarily unavailable",
            "Registration could not be completed. Please try again later.",
            "registration_unavailable",
            request
        );
    }

    @ExceptionHandler(ProfileServiceIntegrationException.class)
    ResponseEntity<ProblemDetail> handleBadGateway(
        ProfileServiceIntegrationException exception,
        HttpServletRequest request
    ) {
        log.error("Profile Service contract failure", exception);
        return response(
            HttpStatus.BAD_GATEWAY,
            "Profile Service integration error",
            "Profile Service returned an unexpected response.",
            "profile_service_error",
            request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> handleBodyValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        List<FieldViolation> violations = exception.getBindingResult().getFieldErrors().stream()
            .map(error -> new FieldViolation(error.getField(), error.getDefaultMessage()))
            .toList();
        return validationResponse(violations, request);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<ProblemDetail> handleMethodValidation(
        HandlerMethodValidationException exception,
        HttpServletRequest request
    ) {
        List<FieldViolation> violations = exception.getParameterValidationResults().stream()
            .flatMap(result -> result.getResolvableErrors().stream()
                .map(error -> new FieldViolation(
                    result.getMethodParameter().getParameterName(),
                    error.getDefaultMessage())))
            .toList();
        return validationResponse(violations, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ProblemDetail> handleConstraintViolation(
        ConstraintViolationException exception,
        HttpServletRequest request
    ) {
        List<FieldViolation> violations = exception.getConstraintViolations().stream()
            .map(violation -> new FieldViolation(
                violation.getPropertyPath().toString(), violation.getMessage()))
            .toList();
        return validationResponse(violations, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ProblemDetail> handleUnreadableBody(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        return response(
            HttpStatus.BAD_REQUEST,
            "Malformed request",
            "Request body is missing or malformed.",
            "malformed_request",
            request
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    ResponseEntity<ProblemDetail> handleMissingHeader(
        MissingRequestHeaderException exception,
        HttpServletRequest request
    ) {
        return validationResponse(
            List.of(new FieldViolation(exception.getHeaderName(), "must be provided")),
            request
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleUnexpected(Exception exception, HttpServletRequest request) {
        log.error("Unhandled request failure", exception);
        return response(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error",
            "An unexpected error occurred.",
            "internal_error",
            request
        );
    }

    private static ResponseEntity<ProblemDetail> validationResponse(
        List<FieldViolation> violations,
        HttpServletRequest request
    ) {
        ResponseEntity<ProblemDetail> response = response(
            HttpStatus.BAD_REQUEST,
            "Request validation failed",
            "One or more request fields are invalid.",
            "validation_failed",
            request
        );
        response.getBody().setProperty("violations", violations);
        return response;
    }

    private static ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String title,
        String detail,
        String code,
        HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("urn:problem:" + code));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", code);
        return ResponseEntity.status(status).body(problem);
    }

    private record FieldViolation(String field, String message) { }
}
