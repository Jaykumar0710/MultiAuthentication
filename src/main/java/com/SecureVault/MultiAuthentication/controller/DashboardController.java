package com.SecureVault.MultiAuthentication.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

//    @GetMapping("/dashboard")
//    public String dashboard(Authentication auth, Model model){
//        model.addAttribute("username", auth.getName());
//        return "dashboard";
//    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model, Principal principal) {
//        model.addAttribute("username", principal.getName());
//        return "admin/dashboard";
//    }


}
