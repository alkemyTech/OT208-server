package com.alkemy.ong.services;

import com.alkemy.ong.exeptions.EmailNotSendException;
import com.sendgrid.Response;

import java.io.IOException;

public interface EmailService {

    public Response sendEmailRegister(String emailUser) throws IOException, EmailNotSendException;

    public Response sendEmailContactForm(String emailUser) throws IOException, EmailNotSendException;

}
