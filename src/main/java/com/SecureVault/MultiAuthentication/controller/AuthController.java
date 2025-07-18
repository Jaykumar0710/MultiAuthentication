package com.SecureVault.MultiAuthentication.controller;

import com.SecureVault.MultiAuthentication.entity.User;
import com.SecureVault.MultiAuthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // ✅ Show Registration Form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    // ✅ Process Registration
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        try {
            userService.register(user);
            redirectAttributes.addFlashAttribute("message", "Registration successful! You can now log in.");
            return "redirect:/login";
        } catch (DataIntegrityViolationException e) {
            // Email already exists (unique constraint violation)
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "register";
        }
    }

    // ✅ Show Login Page
    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    // ✅ Redundant /logout mapping removed (already handled by Spring Security)
}

/**
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

 */