package org.example.eventbookingsystem.domain.Service.Service;

import jakarta.annotation.PostConstruct;
import org.example.eventbookingsystem.security.repository.UserRepository;
import org.example.eventbookingsystem.security.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InitService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public InitService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {
        User user = new User();
        user.setUsername("seha");
        user.setPassword(passwordEncoder.encode("seha1234"));
        user.setRoles(Set.of("ADMIN"));
        user.setEnabled(true);
        
        if (!userRepository.existsByUsername("seha")) {
            userRepository.save(user);
        }
    }
}
