package com.alkemy.ong.services;

import com.sendgrid.Response;
import java.io.IOException;

public interface EmailService {

    public Response sendEmailRegister(String emailUser) throws IOException;

    public Response sendEmailContactForm(String emailUser) throws IOException;

}
