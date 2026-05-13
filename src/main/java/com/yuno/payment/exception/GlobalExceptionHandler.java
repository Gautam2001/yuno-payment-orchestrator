package com.yuno.payment.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yuno.payment.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// PAYMENT NOT FOUND
	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex,
			HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value()).error(HttpStatus.NOT_FOUND.name()).message(ex.getMessage())
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	// INVALID ARGUMENTS
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
			HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).error(HttpStatus.BAD_REQUEST.name()).message(ex.getMessage())
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// VALIDATION ERRORS
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		String validationErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(", "));

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).error(HttpStatus.BAD_REQUEST.name()).message(validationErrors)
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// GENERIC EXCEPTION
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).error(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}