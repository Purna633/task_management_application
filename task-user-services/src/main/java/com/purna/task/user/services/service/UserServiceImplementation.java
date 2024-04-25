package com.purna.task.user.services.service;

import com.purna.task.user.services.config.JwtProvider;
import com.purna.task.user.services.model.User;
import com.purna.task.user.services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation  implements  UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUserProfile(String jwt) {
        String email=JwtProvider.getEmailFromJwtToken(jwt);
        return  userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
