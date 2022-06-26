package com.alkemy.ong.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NewsNotExistException extends RuntimeException {

    private static final long serialVersionUID = -4911335968754555710L;

    public NewsNotExistException(String newsId) {
        super("The news with the id: " + newsId + " does not exist");
    }

}
