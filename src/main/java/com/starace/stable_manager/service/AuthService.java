package com.starace.stable_manager.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.ForgotPasswordRequest;
import com.starace.stable_manager.dto.LoginRequest;
import com.starace.stable_manager.dto.RegisterRequest;
import com.starace.stable_manager.dto.ResetPasswordRequest;
import com.starace.stable_manager.enums.Role;
import com.starace.stable_manager.model.PasswordResetToken;
import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.PasswordResetTokenRepository;
import com.starace.stable_manager.repository.UserRepository;
import com.starace.stable_manager.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    // The send email one
    public void forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()) {
            // Don't do anything
            return;
        }

        User user = userOptional.get();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    // The password reset function
    public void resetPassword(ResetPasswordRequest request) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(request.getToken());

        if(tokenOptional.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        PasswordResetToken resetToken = tokenOptional.get();

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    public String login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);
        } catch(BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
