/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.service.EmailService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.mail.SimpleMailMessage
 *  org.springframework.mail.javamail.JavaMailSenderImpl
 *  org.springframework.scheduling.annotation.Async
 *  org.springframework.stereotype.Service
 */
package com.smartcom.api.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        this.mailSender = new JavaMailSenderImpl();
        this.mailSender.setHost("smtp.hostinger.com");
        this.mailSender.setPort(587);
        this.mailSender.setUsername("support@nimbusengineering.co.bw");
        this.mailSender.setPassword("56Support10*#&$");
        Properties props = this.mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        this.mailSender.send(email);
    }
}

