package com.currency.forex.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ForexExceptionHandler extends ResponseEntityExceptionHandler {
	/**
	 * Exception Handler to handle all Bad Requests
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value= {ForexRequestException.class})
	public ResponseEntity<Object> handleForexRequestException(ForexRequestException e, WebRequest web) {
		final String title = "INVALID INPUT ARGUMENTS";
		// Payload containing custom exception
		ForexInputException forexInputException = new ForexInputException(title, e.getMessage(), HttpStatus.BAD_REQUEST,
				LocalDateTime.now(),web.getDescription(false));

		// Send ResponseEntity
		return new ResponseEntity<>(forexInputException, HttpStatus.BAD_REQUEST);
	}
	/**
	 * Invalid argument exception handler
	 * @param e
	 * @param web
	 * @return
	 */
	@ExceptionHandler(value= {MethodArgumentTypeMismatchException.class})
	public ResponseEntity<?> handleForexBadArgumentExceptions(Exception e, WebRequest web) {
		final String title = "INVALID INPUT ARGUMENTS - input arguments NOT in a right format";
		// Payload containing custom exception
		ForexInputException forexInputException = new ForexInputException(title, e.getMessage(), HttpStatus.BAD_REQUEST,
				LocalDateTime.now(),web.getDescription(false));
		// Send ResponseEntity
		return new ResponseEntity<>(forexInputException, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Invalid argument exception handler
	 * @param e
	 * @param web
	 * @return
	 */
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<?> handleForexGeneralExceptions(Exception e, WebRequest web) {
		final String title = "INTERNAL SERVICE LAYER ERROR";
		// Payload containing custom exception
		ForexInputException forexInputException = new ForexInputException(title, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
				LocalDateTime.now(),web.getDescription(false));

		// Send ResponseEntity
		return new ResponseEntity<>(forexInputException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
