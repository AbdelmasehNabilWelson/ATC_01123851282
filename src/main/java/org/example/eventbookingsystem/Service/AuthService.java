package org.example.eventbookingsystem.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.dto.LoginRequestDTO;
import org.example.eventbookingsystem.dto.LoginResponseDTO;
import org.example.eventbookingsystem.dto.SignUpRequest;
import org.example.eventbookingsystem.repository.UserRepository;
import org.example.eventbookingsystem.security.JWTUtil;
import org.example.eventbookingsystem.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, 
                      UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        return loginWithCredentials(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
    }

    public LoginResponseDTO loginWithCredentials(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken token = new
                    UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(token);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toList()));

            String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), claims);

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setUsername(userDetails.getUsername());
            loginResponseDTO.setAccessToken(jwtToken);
            loginResponseDTO.setTokenType("Bearer");
            return loginResponseDTO;
        } catch (AuthenticationException e) {
            log.error("Error while authenticating user: {}", e.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User signup(SignUpRequest signUpRequest) {
        log.info("Registering new user with username: {}", signUpRequest.getUsername());

        // Check if username or email already exists
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            log.error("Username {} already exists", signUpRequest.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            log.error("Email {} already exists", signUpRequest.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(true); // Enable user by default

        // Set default role
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with username: {}", savedUser.getUsername());

        return savedUser;
    }

    public LoginResponseDTO signupAndLogin(SignUpRequest signUpRequest) {
        // Register the user
        User user = signup(signUpRequest);

        // Generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getAuthorities());

        String jwtToken = jwtUtil.generateToken(user.getUsername(), claims);

        // Create login response
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUsername(user.getUsername());
        loginResponseDTO.setAccessToken(jwtToken);
        loginResponseDTO.setTokenType("Bearer");

        return loginResponseDTO;
    }
}
