package com.alkemy.ong.services;

import java.io.IOException;

import com.alkemy.ong.exceptions.EmailNotSendException;
import com.sendgrid.Response;

public interface EmailService {
	
	 public Response sendEmailRegister(String emailUser) throws IOException, EmailNotSendException;
	 
	 public Response sendEmailContactForm(String emailUser) throws IOException, EmailNotSendException;

}
