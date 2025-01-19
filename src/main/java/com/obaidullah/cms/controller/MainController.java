package com.obaidullah.cms.controller;


import com.obaidullah.cms.service.RoleService;
import com.obaidullah.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class MainController {
    @Autowired
    UserService userservice;
    @Autowired
    RoleService roleservice;

    @GetMapping("/404")
    public String PageNotFound(){
        return "404";
    }
    @GetMapping("/401")
    public String Unauthorized(){
        return "401";
    }
    @GetMapping("/500")
    public String InternalServerError(){
        return "500";
    }
    @GetMapping("/auth/login")
    public String Login(){
        return "login";
    }

    @GetMapping("/auth/forgot")
    public String Forgot(){
        return "password";
    }
    @GetMapping("/table")
    public String Table(){
        return "tables";
    }
    @GetMapping("/index")
    public String Index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=userservice.getUserByEmail(authentication.getName()).getFirstName() +" "+userservice.getUserByEmail(authentication.getName()).getLastName();
        model.addAttribute("contentTemplate", "dashboard");
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("username", username);
        return "templet";
    }





}
