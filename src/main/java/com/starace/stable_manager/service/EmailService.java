package com.starace.stable_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.HorseAlert;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // Working on
    @Async
    public void sendOverdueEmail(String to, List<HorseAlert> alertMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        String textMessage = formatOverdueEmail(alertMessage);

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("You have horses overdue for appointments");
        message.setText(textMessage);

        // mailSender.send(message);
    }

    private String formatOverdueEmail(List<HorseAlert> alertMessage) {
        return "";
    }
}
