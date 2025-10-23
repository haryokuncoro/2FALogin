package com.project._FALogin.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service @Log4j2
public class EmailService {
    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;
    @Value("${SENDGRID_FROM_EMAIL}")
    private String sendGridFromEmail;


    public void sendEmail(String to, String subject, String contentText) throws IOException {
        Email from = new Email(sendGridFromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Email sent: {}", response.getStatusCode());
        } catch (IOException ex) {
            log.error("fail to send email to {}", to, ex);
            throw ex;
        }
    }
}