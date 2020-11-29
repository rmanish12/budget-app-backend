package com.budget.app.controller;

import com.budget.app.entity.User;
import com.budget.app.exceptions.IncorrectDetailsException;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.jwt.service.JwtService;
import com.budget.app.model.Response;
import com.budget.app.model.user.ForgotPasswordRequest;
import com.budget.app.model.user.LoginRequest;
import com.budget.app.model.user.LoginResponse;
import com.budget.app.model.user.UserInfoOnLogin;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody User user) throws Exception {

        User newUser = user;
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        logger.info("[UserController.java] - " + ResponseMessage.USER_CREATION_REQUEST.toString() + " - " + user);

        Response response = new Response();

        try {

            // calling service method to register user
            userService.registerUser(user);

            logger.info("[UserController.java] - " + ResponseMessage.USER_CREATION_SUCCESS.toString());

        } catch (Exception e) {
            logger.error("[UserController.java] - " + ResponseMessage.USER_CREATION_ERROR + " = " + e.getMessage(), e);
            throw e;
        }

        response.setMessage(ResponseMessage.USER_CREATION_SUCCESS.toString());
        response.setStatusCode(HttpStatus.OK.value());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {

        LoginResponse loginResponse = new LoginResponse();

        try {

            logger.info("[UserController.java] - " + ResponseMessage.USER_AUTHENTICATION_REQUEST.toString());

            // authenticate the user with the user provided email and password
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // get user details using email provided
            UserDetails userDetails = userDetailsService
                                            .loadUserByUsername(loginRequest.getEmail());

            // generating token from the user retrieved
            String token = jwtService.generateToken(userDetails);

            User user = userService.findUserByEmail(userDetails.getUsername()).get();

            loginResponse.setAuthToken(token);

            UserInfoOnLogin userInfo = new UserInfoOnLogin(user.getId(), user.getFirstName(), user.getRole());

            loginResponse.setUser(userInfo);

            logger.info("[UserController.java] - " + ResponseMessage.USER_AUTHENTICATED + user.getEmail());

        } catch (BadCredentialsException e) {
            logger.error("[UserController.java] - " + ResponseMessage.BAD_CREDENTIALS.toString(), e);
            throw e;
        } catch (Exception e) {
            logger.error("[UserController.java] - " + ResponseMessage.USER_AUTHENTICATION_FAILURE.toString(), e);
            throw e;
        }

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/who")
    public ResponseEntity<LoginResponse> checkUser(@RequestHeader("authorization") String header) throws Exception {

        LoginResponse loginResponse;

        try {

            logger.info("[UserController.java] - " + ResponseMessage.USER_AUTHENTICATION_REQUEST.toString());
            // getting token from header
            String token = header.substring(7);

            // extracting email from token
            String email = jwtService.extractUsername(token);

            // getting user details from email
            User user = userService.findUserByEmail(email).get();

            loginResponse = new LoginResponse();
            loginResponse.setAuthToken(token);

            UserInfoOnLogin userInfo = new UserInfoOnLogin(user.getId(), user.getFirstName(), user.getRole());

            loginResponse.setUser(userInfo);

            logger.info("[UserController.java] - " + ResponseMessage.USER_AUTHENTICATED + user.getEmail());

        } catch (Exception e) {
            logger.error("[UserController.java] - " + ResponseMessage.USER_AUTHENTICATION_FAILURE.toString(), e);
            throw e;
        }

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/forgot")
    public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPasswordRequest request) throws Exception {

        Response response;

        try {
            logger.info("[UserController.java] - " + ResponseMessage.FORGOT_PASSWORD_REQUEST.toString() + " - " + request.getEmail());

            // encryption password
            request.setPassword(passwordEncoder.encode(request.getPassword()));

            userService.forgotPassword(request);

            logger.info("[UserController.java] - " + ResponseMessage.PASSWORD_RESET_SUCCESS.toString());
        } catch (NotFoundException | IncorrectDetailsException e) {
            logger.error("[UserController.java] - " + ResponseMessage.PASSWORD_RESET_FAILURE + ": " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("[UserController.java] - " + ResponseMessage.PASSWORD_RESET_FAILURE.toString(), e);
            throw e;
        }

        response = new Response(HttpStatus.OK.value(), ResponseMessage.PASSWORD_RESET_SUCCESS.toString(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
