package com.SecureVault.MultiAuthentication.service;

import com.SecureVault.MultiAuthentication.entity.Otp;
import com.SecureVault.MultiAuthentication.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepo;

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(String email){
        String otp = String.valueOf(new Random().nextInt(999999));
        Otp otpEntity = new Otp(null,email,otp, LocalDateTime.now(),false);
        otpRepo.save(otpEntity);
        emailService.sendOtpEmail(email,otp);
    }

    public boolean verifyOtp(String email, String userOtp){
        return otpRepo.findTopByEmailOrderByCreatedAtDesc(email)
                .filter(otp -> !otp.isUsed())
                .filter(otp -> otp.getOtp().equals(userOtp))
                .filter(otp -> otp.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5)))
                .map(validOtp -> {
                    validOtp.setUsed(true);
                    otpRepo.save(validOtp);
                    return true;
                }).orElse(false);
    }

    @Scheduled(fixedRate = 600000) // Every 10 mins
    public void deleteExpiredOtps() {
        List<Otp> otps = otpRepo.findAll();
        otps.stream()
                .filter(otp -> otp.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5)) && !otp.isUsed())
                .forEach(otpRepo::delete);
    }

}
