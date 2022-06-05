package com.alkemy.ong.exeptions;

public class EmailNotSendException extends RuntimeException {

	private static final long serialVersionUID = -1931921297989218107L;

	public EmailNotSendException(String message) {
		super(message);
	}
}
