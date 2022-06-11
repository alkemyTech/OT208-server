package com.alkemy.ong.exeptions;

public class UserNotExistException extends RuntimeException {

	private static final long serialVersionUID = 2916180909128291647L;

	public UserNotExistException(String userId) {
		super("The user with the id: " + userId + " does not exist");
	}
}
