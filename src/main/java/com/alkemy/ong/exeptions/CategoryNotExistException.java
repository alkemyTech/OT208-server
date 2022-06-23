package com.alkemy.ong.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotExistException extends RuntimeException {

	private static final long serialVersionUID = 2172553420575443417L;

	public CategoryNotExistException(String categoryId) {
		super("The category with the id: " + categoryId + " does not exist");
	}

}
