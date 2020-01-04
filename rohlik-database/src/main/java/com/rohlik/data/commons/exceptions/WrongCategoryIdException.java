package com.rohlik.data.commons.exceptions;

public class WrongCategoryIdException extends Exception {	
	public WrongCategoryIdException(String message) {
		super(message);		
	}

	public WrongCategoryIdException(Throwable cause) {
		super(cause);		
	}

	public WrongCategoryIdException(String message, Throwable cause) {
		super(message, cause);		
	}

	private static final long serialVersionUID = 5755991357161808775L;

}
