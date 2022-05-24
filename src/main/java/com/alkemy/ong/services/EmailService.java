package com.alkemy.ong.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final SendGrid sendGrid;

	@Value("${myemail}")
	private String myEmail;

	public Response sendEmailRegister(String emailUser) throws IOException {
		String subject = "¡¡Welcome!!";
		String emailContent = "We welcome you to our platform, thank you for registering!!";
		String emailType = "text/plain";

		return sendEmail(emailUser, subject, emailContent, emailType);
	}

	public Response sendEmailContactForm(String emailUser) throws IOException {
		String subject = "Thank you for contacting us.";
		String emailContent = "Thank you for contacting us. We will be reading your request and we will contact you";
		String emailType = "text/plain";

		return sendEmail(emailUser, subject, emailContent, emailType);
	}

	private Response sendEmail(String emailUser, String emailSubject, String emailContent, String emailType)
			throws IOException {
		Email from = new Email(myEmail);
		String subject = emailSubject;
		Email to = new Email(emailUser);
		Content content = new Content(emailType, emailContent);
		Mail mail = new Mail(from, subject, to, content);
		Request request = new Request();
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());

		return sendGrid.api(request);
	}
}
