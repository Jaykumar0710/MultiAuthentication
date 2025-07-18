package com.SecureVault.MultiAuthentication.service;

import com.SecureVault.MultiAuthentication.entity.User;
import com.SecureVault.MultiAuthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
@Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public  void register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        repo.save(user);
    }
}
