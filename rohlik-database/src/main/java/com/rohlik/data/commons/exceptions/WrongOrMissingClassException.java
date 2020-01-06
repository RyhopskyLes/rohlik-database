package com.rohlik.data.commons.exceptions;

public class WrongOrMissingClassException extends Exception {

	private static final long serialVersionUID = 7374234843332131993L;

	public WrongOrMissingClassException() {
		super();
		
	}

	public WrongOrMissingClassException(String message, Throwable cause) {
		super(message, cause);		
	}

	public WrongOrMissingClassException(String message) {
		super(message);
		
	}

	public WrongOrMissingClassException(Throwable cause) {
		super(cause);		
	}
	

}
