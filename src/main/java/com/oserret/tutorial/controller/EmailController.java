package com.oserret.tutorial.controller;

import ch.qos.logback.classic.Logger;
import com.oserret.tutorial.service.EmailService;
import com.oserret.tutorial.service.StorageService;
import com.oserret.tutorial.utils.Globals;
import com.oserret.tutorial.utils.OrchestratorProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/documents")
public class EmailController {

    private final StorageService storageService;
    private final EmailService emailService;
    private OrchestratorProperties orchestratorProperties = null;
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingController.class);

    @Autowired
    public EmailController(StorageService storageService, EmailService emailService, OrchestratorProperties orchestratorProperties) {
        this.storageService = storageService;
        this.emailService = emailService;
        this.orchestratorProperties = orchestratorProperties;
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam(name = "week", required = false, defaultValue = "1") String week, @RequestParam(name = "year", required = false, defaultValue = "2022") String year) {
        logger.info("Call: /sendEmail SERVICE");
        try {
            logger.info("\n****************************\n");
            logger.info("          Properties:");
            logger.info("\n****************************\n");
            logger.info("MailSmtpAuth: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpStarttlsEnable: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpHost: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpPort: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailSmtpSslTrust: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailUsername: " + orchestratorProperties.getMailSmtpAuth());
            logger.info("MailPwd: ************");

            emailService.sendEmail(orchestratorProperties.getMailSmtpAuth(),
                    orchestratorProperties.getMailSmtpSslProtocols(),
                    orchestratorProperties.getMailSmtpStarttlsEnable(),
                    orchestratorProperties.getMailSmtpHost(),
                    orchestratorProperties.getMailSmtpPort(),
                    orchestratorProperties.getMailSmtpSslTrust(),
                    orchestratorProperties.getMailUsername(),
                    orchestratorProperties.getMailPwd(),
                    orchestratorProperties.getOutputZipLocation() + orchestratorProperties.getZipFileName() + Globals.ZIP_OUTPUT_EXTENSION,
                    orchestratorProperties.getMailForAddress(),
                    week,
                    year);
        } catch (MessagingException|IOException ex) {
            logger.error(ex.getMessage());
        }
        return "Email sent";
    }

}
