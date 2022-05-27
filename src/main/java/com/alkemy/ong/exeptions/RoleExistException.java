package com.alkemy.ong.exeptions;

public class RoleExistException extends RuntimeException {

    public RoleExistException(String message) {
        super(message);
    }

}
