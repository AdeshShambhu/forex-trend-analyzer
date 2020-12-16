package com.currency.forex.exception;


/**
 * Custom Exception
 * @author adesh
 *
 */
public class ForexRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForexRequestException(String message) {
		super(message);
	}
	
	public ForexRequestException(String message, Throwable cause) {
		super(message,cause);
	}
}
