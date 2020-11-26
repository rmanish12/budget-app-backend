package com.budget.app.service;

import com.budget.app.entity.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> findUserByEmail(String email);
    public void registerUser(User user) throws Exception;

}
