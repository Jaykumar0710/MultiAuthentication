package com.SecureVault.MultiAuthentication.controller;

import com.SecureVault.MultiAuthentication.service.OtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TwoFactorController {

    @Autowired
    private OtpService otpService;

    // ✅ 2FA OTP Verify Page
    @GetMapping("/2fa-verify")
    public String showOtpPage(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "message", required = false) String message,
                              Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid or expired OTP.");
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "2fa-verify";
    }

    // ✅ OTP Verification Logic
    @PostMapping("/verify-2fa")
    public String verifyOtp(@RequestParam String otp, HttpSession session, Model model) {
        Authentication auth = (Authentication) session.getAttribute("PRE_AUTH_USER");
        String email = auth.getName();

        if (otpService.verifyOtp(email, otp)) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            session.removeAttribute("PRE_AUTH_USER");
            return "redirect:/dashboard";
        } else {
            return "redirect:/2fa-verify?error";
        }
    }

    // ✅ Resend OTP Logic
    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session) {
        Authentication auth = (Authentication) session.getAttribute("PRE_AUTH_USER");
        if (auth == null) {
            return "redirect:/login?error"; // No pending user
        }

        String email = auth.getName();
        otpService.generateAndSendOtp(email); // Regenerate & send OTP

        return "redirect:/2fa-verify?message=OTP sent again to your email";
    }
}
