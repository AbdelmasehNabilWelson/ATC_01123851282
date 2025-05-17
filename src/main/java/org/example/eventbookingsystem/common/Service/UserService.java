package org.example.eventbookingsystem.common.Service;

import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.api.dto.UpdateUserProfileDTO;
import org.example.eventbookingsystem.security.Entity.User;
import org.example.eventbookingsystem.security.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AzureBlobStorageService azureBlobStorageService;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AzureBlobStorageService azureBlobStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @Transactional
    public void updateUser(String username, UpdateUserProfileDTO updateDTO) {
        log.info("Updating user with username: {}", username);
        if (userRepository.existsByUsername(updateDTO.getUsername())) {
            throw new IllegalArgumentException("Username: " + updateDTO.getUsername() + " already exists");
        }

        if (userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new IllegalArgumentException("Email: " + updateDTO.getEmail() + " already exists");
        }

        if (!updateDTO.getNewPassword().matches(updateDTO.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New Password and Confirm Password should match");
        }

        User user = userRepository.findByUsername(username).orElseThrow();


        if (!passwordEncoder.matches(updateDTO.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }


        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(updateDTO.getNewPassword()));
        userRepository.save(user);
    }

    public String uploadImage(MultipartFile image, String username) throws IOException {
        log.info("Uploading image for the user with username: {}", username);
        if (image == null || image.isEmpty()) {
            log.error("Image is null or Empty");
            throw new IllegalArgumentException("Image is empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username: " + username + " not found"));
        String imageURL = "";
        try {
            imageURL = azureBlobStorageService.storeImage(image);
        } catch (IOException e) {
            log.info("Error while uploading the user image: {}", e.getMessage());
            throw new IOException("Error while uploading the user image: " + e.getMessage());
        }

        user.setImageUrl(imageURL);
        userRepository.save(user);

        return imageURL;
    }
}
