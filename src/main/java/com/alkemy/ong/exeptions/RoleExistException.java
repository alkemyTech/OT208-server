package com.alkemy.ong.exeptions;

public class RoleExistException extends RuntimeException {

    private static final long serialVersionUID = -1274878210010431879L;

    public RoleExistException(String message) {
        super(message);
    }

}
