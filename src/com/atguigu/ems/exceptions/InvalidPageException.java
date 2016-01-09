package com.atguigu.ems.exceptions;

public class InvalidPageException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidPageException() {
		super();
	}

	public InvalidPageException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidPageException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPageException(String message) {
		super(message);
	}

	public InvalidPageException(Throwable cause) {
		super(cause);
	}
	
}
