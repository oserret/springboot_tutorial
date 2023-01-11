package com.oserret.tutorial.service;

import ch.qos.logback.classic.Logger;
import com.oserret.tutorial.controller.LoggingController;
import com.oserret.tutorial.utils.Globals;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingController.class);

    @Override
    public void sendEmail(Boolean mailSmtpAuth,
                          String mailSmtpSslProtocols,
                          String mailSmtpStarttlsEnable,
                          String mailSmtpHost,
                          String mailSmtpPort,
                          String mailSmtpSslTrust,
                          String mailUsername,
                          String mailPwd,
                          String zipFile,
                          String mailForAddress,
                          String week,
                          String year) throws MessagingException, IOException {
        logger.info("EmailServiceImpl - sendEmail execution started");

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", mailSmtpAuth);
        prop.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        prop.put("mail.smtp.host", mailSmtpHost);
        prop.put("mail.smtp.port", mailSmtpPort);
        prop.put("mail.smtp.ssl.trust", mailSmtpSslTrust);
        prop.setProperty("mail.smtp.ssl.protocols", mailSmtpSslProtocols);

        logger.info("Creating Session to SMTP Server");
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPwd);
            }
        });

        logger.info("Creating Message");
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("PSInternalProcess@expert.ai"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(mailForAddress));
        message.setSubject(Globals.MAIL_SUBJECT_MESSAGE.replace("*",week).replace("#", year));

        String msg = Globals.MAIL_BODY_MESSAGE.replace("*",week);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        logger.info("Adding attachment to the Message");
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.attachFile(new File(zipFile));

        multipart.addBodyPart(attachmentBodyPart);

        message.setContent(multipart);

        logger.info("Sending Message");
        Transport.send(message);
    }
}
