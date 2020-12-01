package com.budget.app.service;

import com.budget.app.entity.User;
import com.budget.app.model.user.ForgotPasswordRequest;
import com.budget.app.model.user.UpdatePasswordRequest;
import com.budget.app.model.user.UpdateUserRequest;

import java.util.Optional;

public interface UserService {

    public Optional<User> findUserByEmail(String email);
    public void registerUser(User user) throws Exception;
    public void forgotPassword(ForgotPasswordRequest request) throws Exception;
    public void updateUser(int userId, UpdateUserRequest user) throws Exception;
    public void updatePassword(int userId, UpdatePasswordRequest request) throws Exception;

}
