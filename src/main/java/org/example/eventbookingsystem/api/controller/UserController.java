package org.example.eventbookingsystem.api.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.api.dto.UpdateUserProfileDTO;
import org.example.eventbookingsystem.common.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public void UpdateUser(@Valid @RequestBody UpdateUserProfileDTO updateUserProfileDTO, Authentication authentication) {
        log.info("Updating the user profile with username: {}", authentication.getName());
        userService.updateUser(authentication.getName(), updateUserProfileDTO);
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("image")MultipartFile image, Authentication authentication) {
        log.info("Uploading the user image with username: {}", authentication.getName());

        String imageURL = "";
        try {
            imageURL = userService.uploadImage(image, authentication.getName());
        } catch (IOException e) {
            log.info("Failed to upload the user image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the user image: " + e.getMessage());
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            log.info("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to upload the user image: " + e.getMessage());
        }
        return ResponseEntity.ok("User image saved: "+  imageURL);
    }
}
