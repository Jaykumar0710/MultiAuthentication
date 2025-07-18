package com.SecureVault.MultiAuthentication.controller;

import com.SecureVault.MultiAuthentication.service.OtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class OtpLoginController {

    @Autowired
    private OtpService otpService;

    @GetMapping("/otp-login")
    public String showOtpLoginForm() {
        return "otp-login";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email, Model model) {
        otpService.generateAndSendOtp(email);
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            Model model,
                            HttpSession session) {

        boolean valid = otpService.verifyOtp(email, otp);
        if (valid) {
            // ✅ Create manual authentication for OTP-based login
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // ✅ Optional: Store username in session or model
            session.setAttribute("username", email);
            return "redirect:/dashboard";  // ✅ Redirect to role-based dashboard
        } else {
            model.addAttribute("email", email);  // for reloading the form
            model.addAttribute("error", "Invalid or expired OTP.");
            return "verify-otp"; // ✅ typo fixed: was "verify-Otp"
        }
    }
}
