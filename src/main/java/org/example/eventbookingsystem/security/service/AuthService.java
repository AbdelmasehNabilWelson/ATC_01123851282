package org.example.eventbookingsystem.security.service;

import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.api.dto.JwtLogInResponseDTO;
import org.example.eventbookingsystem.api.dto.RegisterRequestDTO;
import org.example.eventbookingsystem.security.Entity.AuthenticationToken;
import org.example.eventbookingsystem.security.repository.AuthenticationTokenRepository;
import org.example.eventbookingsystem.security.repository.UserRepository;
import org.example.eventbookingsystem.security.jwt.JWTUtil;
import org.example.eventbookingsystem.security.Entity.User;
import org.example.eventbookingsystem.common.Service.MailtrapEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailtrapEmailService mailtrapEmailService;
    private final AuthenticationTokenRepository authenticationTokenRepository;

    @Value("${APIServerDomain}")
    private String apiServerDomain;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       UserRepository userRepository, PasswordEncoder passwordEncoder, MailtrapEmailService mailtrapEmailService, AuthenticationTokenRepository authenticationTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailtrapEmailService = mailtrapEmailService;
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    public JwtLogInResponseDTO login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken token = new
                    UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(token);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info("User '{}' logged in successfully", userDetails.getUsername());

            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), claims);

            JwtLogInResponseDTO jwtLogInResponseDTO = new JwtLogInResponseDTO();
            jwtLogInResponseDTO.setUsername(userDetails.getUsername());
            jwtLogInResponseDTO.setAccessToken(jwtToken);
            jwtLogInResponseDTO.setTokenType("Bearer");
            return jwtLogInResponseDTO;
        } catch (AuthenticationException e) {
            log.error("Error while authenticating user: {}", e.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public void signup(RegisterRequestDTO signUpRequest, boolean isAPIRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            log.error("User with username: {} exists. try another one", signUpRequest.getUsername());
            throw new BadCredentialsException("User with username: " + signUpRequest.getUsername() + "exists");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            log.error("User with email: {} exists. try another one", signUpRequest.getEmail());
            throw new BadCredentialsException("User with email: " + signUpRequest.getEmail() + "exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setEnabled(false);
        user.setRoles(Set.of("USER")); // users only are allowed to sign up from apis. admins are manually added by other admins
        userRepository.save(user);

        sendVerificationEmail(user, isAPIRequest);
    }

    private void sendVerificationEmail(User user, Boolean isAPIRequest) {
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(UUID.randomUUID().toString());
        authenticationToken.setUser(user);
        authenticationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        authenticationTokenRepository.save(authenticationToken);


        String subject = "Event Booking System - Verify your email";
        String text = "Click the following link to verify your email" + "\n";

        if (isAPIRequest) {
            text += "http://" + apiServerDomain + "/api/auth/verify?token=" + authenticationToken.getToken();
        } else {
            text += "http://" + apiServerDomain + "/verify?token=" + authenticationToken.getToken();
        }

        mailtrapEmailService.sendEmail(user.getEmail(), subject, text);
    }

    public void verifyUser(String token) {
        AuthenticationToken authToken = authenticationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid verification token"));

        if (authToken.getExpiryDate().isAfter(LocalDateTime.now()))  {
            User user = authToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            authenticationTokenRepository.delete(authToken); // no need to keep the token to save storage.
            log.info("User with username: {} verified successfully", user.getUsername());
            return;
        }

        log.error("Verification token expired for user with username: {}", authToken.getUser().getUsername());
        throw new BadCredentialsException("Verification token expired for user with username: " + authToken.getUser().getUsername());
    }
}
