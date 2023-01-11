package com.oserret.tutorial.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

public interface EmailService {

    void sendEmail(Boolean mailSmtpAuth,
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
                   String year) throws MessagingException, IOException;
}
