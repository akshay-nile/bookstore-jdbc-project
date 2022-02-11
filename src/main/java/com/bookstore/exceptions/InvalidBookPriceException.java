package com.bookstore.exceptions;

@SuppressWarnings("serial")
public class InvalidBookPriceException extends RuntimeException {

	public InvalidBookPriceException(String message) {
		super(message);
	}

}
