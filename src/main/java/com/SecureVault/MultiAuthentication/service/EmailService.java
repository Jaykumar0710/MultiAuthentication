package com.SecureVault.MultiAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
@Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP IS: "+otp +"\n\nThis OTP is Valid for 5 Minutes only");

        mailSender.send(message);
    }

}
