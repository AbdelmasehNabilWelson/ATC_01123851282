package org.example.eventbookingsystem.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.security.service.AuthService;
import org.example.eventbookingsystem.api.dto.RESTUserLoginResponseDTO;
import org.example.eventbookingsystem.api.dto.RESTUserRegistrationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Authorization header format. Basic authentication required.");
        }

        try {
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

            RESTUserLoginResponseDTO response = authService.login(username, password);
            
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
    public ResponseEntity<String> signup(@Valid @RequestBody RESTUserRegistrationDTO signUpRequest) {
        authService.signup(signUpRequest, true); // isAPIRequest set to true, because it is an API request
        return ResponseEntity.ok().body("Check your email for verification Link");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token) {
        authService.verifyUser(token);
        return ResponseEntity.ok().body("You are verified successfully, you can now login");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Logging out");
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Logged out successfully");
    }
}
