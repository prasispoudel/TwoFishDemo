package com.example.TwoFishDemo.service;

import com.example.TwoFishDemo.model.User;
import com.example.TwoFishDemo.repository.UserRepository;
import com.example.TwoFishDemo.util.TwoFishUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final String secretkey= "2444666668888888";
    public  void saveUser(User user) throws Exception{
        String encryptedPassword = TwoFishUtil.encrypt(user.getPassword(),secretkey);
        user.setUsername(user.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(user.getEmail());
        user.setRole("USER");
        userRepository.save(user);
    }
    public String encryptPassword(String plainPassword) throws Exception{
        return TwoFishUtil.encrypt(plainPassword, secretkey);
    }
    public String decryptPassword(String encryptedPassword) throws Exception{
        return TwoFishUtil.decrypt(encryptedPassword,secretkey);
    }
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
