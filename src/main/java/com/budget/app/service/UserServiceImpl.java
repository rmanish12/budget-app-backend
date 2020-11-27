package com.budget.app.service;

import com.budget.app.entity.User;
import com.budget.app.exceptions.AlreadyPresentException;
import com.budget.app.repository.UserRepository;
import com.budget.app.responseMessage.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void registerUser(User user) throws Exception {

        logger.info("[UserServiceImpl.java] - " + ResponseMessage.USER_CREATION_REQUEST.toString() + " - " + user);

        try {

            Optional<User> userPresentWithEmail = userRepository.findUserByEmail(user.getEmail());
            Optional<User> userPresentWithId = userRepository.findById(user.getId());

            // checking if the user with given id and email is not present
            if(!userPresentWithEmail.isPresent() && !userPresentWithId.isPresent()) {
                userRepository.save(user);
            }
            // if user is present with the given email OR with the given id, then throw error
            else if(userPresentWithEmail.isPresent() || userPresentWithId.isPresent()) {
                throw new AlreadyPresentException(ResponseMessage.USER_ALREADY_PRESENT.toString());
            }

            logger.info("[UserServiceImpl.java] - " + ResponseMessage.USER_CREATION_SUCCESS.toString());

        } catch (Exception e) {
            logger.error("[UserServiceImpl.java] - " + ResponseMessage.USER_CREATION_ERROR.toString(), e);
            throw e;
        }
    }


}
