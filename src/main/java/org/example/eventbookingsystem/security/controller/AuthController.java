package org.example.eventbookingsystem.security.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.security.service.AuthService;
import org.example.eventbookingsystem.dto.LoginResponseDTO;
import org.example.eventbookingsystem.dto.SignUpRequest;
import org.example.eventbookingsystem.security.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Authorization header format. Basic authentication required.");
        }

        try {
            // Extract and decode Basic auth credentials
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
    
            if (values.length != 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Basic authentication format");
            }
    
            String username = values[0];
            String password = values[1];
    
            log.info("Attempting login for user: {}", username);
    
            // Use the authService to handle the authentication
            LoginResponseDTO response = authService.loginWithCredentials(username, password);
            
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDTO> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        LoginResponseDTO response = authService.signupAndLogin(signUpRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Logging out");
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Logged out successfully");
    }
}
