package com.obaidullah.cms.controller;

import com.obaidullah.cms.auth.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String Index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        String username = userService.getUserByEmail(email).getName();

        model.addAttribute("contentTemplate", "dashboard");
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("username", username);

        return "templet";
    }


}
