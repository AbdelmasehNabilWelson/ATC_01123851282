package org.example.eventbookingsystem.web.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.eventbookingsystem.web.dto.UserRegistrationDTO;
import org.example.eventbookingsystem.api.dto.RESTUserRegistrationDTO;
import org.example.eventbookingsystem.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class RegistrationController {

    private final AuthService authService;

    @Autowired
    public RegistrationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("userRegistrationDTO")) {
            model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {


        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userRegistrationDTO", "Passwords do not match");
        }

        if (result.hasErrors()) {
            return "register";
        }

        log.info("Registering user: {}", userRegistrationDTO);

        RESTUserRegistrationDTO restUserRegistrationDTO = new RESTUserRegistrationDTO();
        restUserRegistrationDTO.setUsername(userRegistrationDTO.getUsername());
        restUserRegistrationDTO.setEmail(userRegistrationDTO.getEmail());
        restUserRegistrationDTO.setPassword(userRegistrationDTO.getPassword());

        log.info("Converted RESTUserRegistrationDTO: {}", restUserRegistrationDTO);
        try {
            authService.signup(restUserRegistrationDTO, false); // not an api request, set it to false. because it is a web request
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Please check your email to activate your account.");
            return "redirect:/login";
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        authService.verifyUser(token);
        return "redirect:/login";
    }
}
