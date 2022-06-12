package com.alkemy.ong.exeptions;

public class NewsNotExistException extends RuntimeException {

	private static final long serialVersionUID = -4911335968754555710L;

	public NewsNotExistException(String newsId) {
		super("The news with the id: " + newsId + " does not exist");
	}

}
