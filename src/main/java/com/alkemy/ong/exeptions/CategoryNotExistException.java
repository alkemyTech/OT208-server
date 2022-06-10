package com.alkemy.ong.exeptions;

public class CategoryNotExistException extends RuntimeException {

	private static final long serialVersionUID = 2172553420575443417L;

	public CategoryNotExistException(String categoryId) {
		super("The news with the id: " + categoryId + " does not exist");
	}

}
