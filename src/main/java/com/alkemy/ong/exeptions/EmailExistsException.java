/*
 * 
 */
package com.alkemy.ong.exeptions;

/**
 *
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */
public class EmailExistsException extends RuntimeException {
    
    public EmailExistsException(String message){
        super(message);
    }
    
}