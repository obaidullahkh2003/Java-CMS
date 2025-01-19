package com.obaidullah.cms.controller;

import com.obaidullah.cms.dto.UserDto;
import com.obaidullah.cms.repository.UserRepository;
import com.obaidullah.cms.service.AuthinticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/auth/register")
public class RegisterController {
    UserRepository userRepository;
    AuthinticationService authinticationService;
    @Autowired
    public RegisterController(UserRepository userRepository, AuthinticationService authinticationService) {
        this.userRepository = userRepository;
        this.authinticationService = authinticationService;
    }

    @GetMapping
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("user") @Validated UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (!userDto.isPasswordMatching()) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "register";
        }
        if (authinticationService.isEmailTaken(userDto.getEmail())) {
            result.rejectValue("email", "error.user", "This email is already in use");
            return "register";
        }

        authinticationService.createUser(userDto);
        return "redirect:/auth/login";
    }
}
