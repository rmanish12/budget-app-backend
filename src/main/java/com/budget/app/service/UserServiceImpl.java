package com.budget.app.service;

import com.budget.app.entity.User;
import com.budget.app.exceptions.AlreadyPresentException;
import com.budget.app.exceptions.IncorrectDetailsException;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.user.ForgotPasswordRequest;
import com.budget.app.model.user.GetUserDetailsResponse;
import com.budget.app.model.user.UpdatePasswordRequest;
import com.budget.app.model.user.UpdateUserRequest;
import com.budget.app.repository.UserRepository;
import com.budget.app.responseMessage.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Override
    public void forgotPassword(ForgotPasswordRequest request) throws Exception {

        logger.info("[UserServiceImpl.java] - " + ResponseMessage.FORGOT_PASSWORD_REQUEST.toString() + " - " + request.getEmail());

        try {

            // checking is the user with the given email is present
            Optional<User> userExist = userRepository.findUserByEmail(request.getEmail());

            if (!userExist.isPresent()) {
                throw new NotFoundException(ResponseMessage.USER_NOT_FOUND.toString());
            } else {

                User user = userExist.get();

                // checking if the date of birth provided by the user is correct
                if (!user.getDateOfBirth().isEqual(request.getDateOfBirth())) {
                    throw new IncorrectDetailsException(ResponseMessage.FORGOT_PASSWORD_DETAILS_MISMATCH.toString());
                } else {

                    // updating password
                    user.setPassword(request.getPassword());

                    // saving updated user detail
                    userRepository.save(user);

                    logger.info("[UserServiceImpl.java] - " + ResponseMessage.PASSWORD_RESET_SUCCESS.toString());
                }

            }

        } catch (Exception e) {
            logger.error("[UserServiceImpl.java] - " + ResponseMessage.PASSWORD_RESET_FAILURE.toString(), e);
            throw e;
        }
    }

    @Override
    public void updateUser(int userId, UpdateUserRequest request) throws Exception {

        logger.info("[UserServiceImpl.java] - " + ResponseMessage.UPDATE_USER_REQUEST.toString() + " - " + userId);

        try {
            // check if the user with the given id exist
            Optional<User> userPresent = userRepository.findById(userId);

            // if not present, throw NotFoundException
            if(!userPresent.isPresent()) {
                throw new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString());
            }

            // if user is present, update the user
            User user = userPresent.get();

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setGender(request.getGender());
            user.setDateOfBirth(request.getDateOfBirth());

            userRepository.save(user);

            logger.info("[UserServiceImpl.java] - " + ResponseMessage.UPDATE_USER_SUCCESS.toString());

        } catch (Exception e) {
            logger.error("[UserServiceImpl.java] - " + ResponseMessage.UPDATE_USER_FAILURE.toString(), e);
            throw e;
        }

    }

    @Override
    public void updatePassword(int userId, UpdatePasswordRequest request) throws Exception {

        logger.info("[UserServiceImpl.java] - " + ResponseMessage.UPDATE_PASSWORD_REQUEST.toString() + userId);

        try {
            Optional<User> userPresent = userRepository.findById(userId);

            // if user is not present, throw NotFoundException
            if(!userPresent.isPresent()) {
                throw new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString());
            } else {

                User user = userPresent.get();

                // check if the old password matches the password saved in database
                if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                } else {
                    throw new IncorrectDetailsException(ResponseMessage.OLD_PASSWORD_MISMATCH.toString());
                }

                logger.info("[UserServiceImpl.java] - " + ResponseMessage.PASSWORD_RESET_SUCCESS.toString());

            }
        } catch (Exception e) {
            logger.error("[UserServiceImpl.java] - " + ResponseMessage.PASSWORD_RESET_FAILURE.toString(), e);
            throw e;
        }

    }

    @Override
    public GetUserDetailsResponse getUserDetails(int userId) throws Exception {

        logger.info("[UserServiceImpl.java] - " + ResponseMessage.GET_USER_DETAILS_REQUEST.toString() + userId);

        GetUserDetailsResponse response;

        try {

            Optional<User> userPresent = userRepository.findById(userId);

            // checking if user is present, else throw NotFoundException
            if(!userPresent.isPresent()) {
                throw new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString());
            } else {

                User user = userPresent.get();

                response = new GetUserDetailsResponse();
                response.setEmail(user.getEmail());
                response.setFirstName(user.getFirstName());
                response.setLastName(user.getLastName());
                response.setDateOfBirth(user.getDateOfBirth());
                response.setGender(user.getGender());

                logger.info("[UserServiceImpl.java] - " + ResponseMessage.GET_USER_DETAILS_SUCCESS.toString());

            }

        } catch (Exception e) {
            logger.error("[UserServiceImpl.java] - " + ResponseMessage.GET_USER_DETAILS_FAILURE.toString());
            throw e;
        }

        return response;

    }
}
