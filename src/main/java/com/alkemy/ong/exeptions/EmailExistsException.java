package com.alkemy.ong.exeptions;

public class EmailExistsException extends RuntimeException {

    private static final long serialVersionUID = -1577718358695273022L;

    public EmailExistsException(String message) {
        super(message);
    }

}
