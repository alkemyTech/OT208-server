package com.alkemy.ong.exceptions;

/**
 * @author nagredo
 * @project OT208-server
 * @class ArgumentRequiredException
 */
public class ArgumentRequiredException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ArgumentRequiredException(String mensaje) {
        super(mensaje);
    }

}
