package com.starace.stable_manager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.HorseAlert;
import com.starace.stable_manager.dto.StableInviteRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendOverdueEmail(String to, List<HorseAlert> alertMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        String textMessage = formatOverdueEmail(alertMessage);

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("You have horses overdue for appointments");
        message.setText(textMessage);

        mailSender.send(message);
    }

    @Async
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Reset your password");
        message.setText("Link to reset your password:\n" + resetLink);

        mailSender.send(message);
    }

    @Async
    public void sendStableInviteEmail(StableInviteRequest request, String resetLink, String stableName) {
        SimpleMailMessage message = new SimpleMailMessage();

        String toEmail = request.getEmail();

        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Invite to join " + stableName);
        message.setText("Link to join " + stableName + ": " + resetLink);

        mailSender.send(message);
    }

    private String formatOverdueEmail(List<HorseAlert> alertMessage) {
        StringBuilder formattedMessage = new StringBuilder();

        alertMessage.forEach(alert -> {
            formattedMessage
                .append(alert.getHorseName()).append(" is overdue for: ");

            String formattedAlerts = alert.getAlerts().stream()
                .map(type -> type.name())
                .collect(Collectors.joining(", "));

            formattedMessage.append(formattedAlerts).append('\n');
        });

        return formattedMessage.toString();
    }
}
