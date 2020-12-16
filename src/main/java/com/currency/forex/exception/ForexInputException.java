package com.currency.forex.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

/**
 * Custom Exception to display all the necessary error information in the Response.
 * @author adesh
 *
 */
public class ForexInputException{
	
	private final String title;
	private final String message;
	//NO need to show the complete error stack trace
//	private final Throwable throwable;
	private final HttpStatus httpStatus;
	private final LocalDateTime timestamp;
	private final String path;

	public ForexInputException(String title, String message, HttpStatus httpStatus, LocalDateTime timestamp, String path) {
		this.path = path;
		this.title = title;
		this.message = message;
//		this.throwable = throwable;
		this.httpStatus = httpStatus;
		this.timestamp = timestamp;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getMessage() {
		return message;
	}

//	public Throwable getThrowable() {
//		return throwable;
//	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getPath() {
		return path;
	}
	
	
	
	
}
