package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.EmailPin;
import org.example.entities.User;
import org.example.enums.ERole;
import org.example.exceptions.EmailPinNotFoundException;
import org.example.exceptions.InvalidPasswordException;
import org.example.exceptions.InvalidPinException;
import org.example.pojo.*;
import org.example.repositories.EmailsPinsRepository;
import org.example.repositories.UserRepository;
import org.example.services.AuthenticationService;
import org.example.services.EmailService;
import org.example.services.JwtService;
import org.example.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailsPinsRepository emailsPinsRepository;
    private final UserRepository userRepository;


    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .email(request.getUsername())
                .username(request.getName())
                .password(passwordEncoder.encode(request
                        .getPassword())).role(ERole.ROLE_CLIENT)
                .isConfirmed(false)
                .newsSubscribed(request.getNewsSubscribed())
                .build();

        userService.create(user);

        sendValidationMsgToEmail(user.getEmail());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }


    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not fount by username: " + request.getUsername()));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getUsername());

            var jwt = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(jwt);
        } else {
            throw new InvalidPasswordException(request.getUsername());
        }
    }

    @Override
    public void sendValidationMsgToEmail(String email) {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        emailService.sendSimpleEmail(
                email,
                "Подтверждение почты",
                "Для подтверждения почты введите пин код: " + randomNumber);
        Optional<EmailPin> emailPin = emailsPinsRepository.findByEmail(email);
        if (emailPin.isEmpty()) emailsPinsRepository.save(new EmailPin(email, randomNumber));
        else emailsPinsRepository.save(emailPin.get().setPin(randomNumber));
    }

    @Override
    public void validateEmail(String email, Long pin) {
        EmailPin emailPin = emailsPinsRepository.findByEmail(email)
                .orElseThrow(() -> new EmailPinNotFoundException(email));
        if (emailPin.getPin() == pin) {
            emailsPinsRepository.delete(emailPin);
            User user = userRepository.findByUsername(email)
                    .orElseThrow(() -> new UsernameNotFoundException(email));
            user.setIsConfirmed(true);
            userRepository.save(user);
        } else throw new InvalidPinException(pin);
    }

    @Override
    public Boolean doesUserAlreadyExist(String email) {
        return userRepository.existsByUsername(email);
    }

    @Override
    public Long changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByUsername(changePasswordRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(changePasswordRequest.getUsername()));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            return userRepository.save(user).getId();
        } else {
            throw new InvalidPasswordException(user.getEmail());
        }
    }

    @Override
    public void forgotPassword(String email) {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        emailService.sendSimpleEmail(
                email,
                "Восстановление доступа к аккаунту",
                "Для смены пароля введите пин код: " + randomNumber);
        Optional<EmailPin> emailPin = emailsPinsRepository.findByEmail(email);
        if (emailPin.isEmpty()) emailsPinsRepository.save(new EmailPin(email, randomNumber));
        else emailsPinsRepository.save(emailPin.get().setPin(randomNumber));
    }

    @Override
    public Long changeForgottenPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Long pin = forgotPasswordRequest.getPin();
        EmailPin emailPin = emailsPinsRepository.findByEmail(forgotPasswordRequest.getUsername())
                .orElseThrow(() -> new EmailPinNotFoundException(forgotPasswordRequest.getUsername()));
        if (emailPin.getPin() == pin) {
            emailsPinsRepository.delete(emailPin);
            User user = userRepository.findByUsername(forgotPasswordRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(forgotPasswordRequest.getUsername()));
            user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
            return userRepository.save(user).getId();
        } else throw new InvalidPinException(pin);

    }
}