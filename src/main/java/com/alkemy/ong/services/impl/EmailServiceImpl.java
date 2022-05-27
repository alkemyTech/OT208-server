package com.alkemy.ong.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alkemy.ong.services.EmailService;
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
public class EmailServiceImpl implements EmailService {
	private final SendGrid sendGrid;

	@Value("${email-sendgrid}")
	private String myEmail;

	@Value("${email.template.register}")
	private String templateRegister;

	@Override
	public Response sendEmailRegister(String emailUser) throws IOException {
		String subject = "¡¡Welcome!!";
		String emailContent = "We welcome you to our platform, thank you for registering!!";
		String emailType = "text/html";

		return sendEmail(emailUser, subject, emailContent, emailType, templateRegister);
	}

	@Override
	public Response sendEmailContactForm(String emailUser) throws IOException {
		String subject = "Thank you for contacting us.";
		String emailContent = "Thank you for contacting us. We will be reading your request and we will contact you";
		String emailType = "text/html";

		return sendEmail(emailUser, subject, emailContent, emailType, null);
	}

	private Response sendEmail(String emailUser, String emailSubject, String emailContent, String emailType,
			String template) throws IOException {
		Mail mail = buildMail(emailType, emailContent, emailUser, emailSubject, template);

		Request request = new Request();
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());

		return sendGrid.api(request);
	}

	private Mail buildMail(String emailType, String emailContent, String emailUser, String emailSubject,
			String template) {
		Email from = new Email(myEmail);
		Email to = new Email(emailUser);
		Content content = new Content(emailType, emailContent);
		Mail mail = new Mail(from, emailSubject, to, content);
		mail.setTemplateId(template);

		return mail;
	}
}
