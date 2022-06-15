package com.alkemy.ong.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotExistException extends RuntimeException {

	private static final long serialVersionUID = 2916180909128291647L;

	public UserNotExistException(String userId) {
		super("The user with the id: " + userId + " does not exist");
	}
}
