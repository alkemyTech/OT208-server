package com.alkemy.ong.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotSendException extends RuntimeException {

	private static final long serialVersionUID = -1931921297989218107L;

	public EmailNotSendException(String message) {
		super(message);
	}
}
