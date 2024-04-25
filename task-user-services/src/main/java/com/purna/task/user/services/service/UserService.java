package com.purna.task.user.services.service;

import com.purna.task.user.services.model.User;

import java.util.List;

public interface UserService {
    public User getUserProfile(String jwt);

    public List<User> getAllUser();
}
