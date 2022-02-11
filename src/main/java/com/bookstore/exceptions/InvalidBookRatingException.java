package com.bookstore.exceptions;

@SuppressWarnings("serial")
public class InvalidBookRatingException extends RuntimeException {

	public InvalidBookRatingException(String message) {
		super(message);
	}

}
