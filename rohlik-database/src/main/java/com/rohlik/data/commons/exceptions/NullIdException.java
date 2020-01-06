package com.rohlik.data.commons.exceptions;

public class NullIdException  extends Exception {	
	private static final long serialVersionUID = 8237901053748939995L;
	
	public NullIdException() {
		super();		
	}

	public NullIdException(String message, Throwable cause) {
		super(message, cause);		
	}

	public NullIdException(String message) {
		super(message);		
	}

	public NullIdException(Throwable cause) {
		super(cause);
		
	}

	

}
