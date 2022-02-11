package com.bookstore.exceptions;

@SuppressWarnings("serial")
public class InvalidBookNameException extends RuntimeException {

	public InvalidBookNameException(String message) {
		super(message);
	}

}
