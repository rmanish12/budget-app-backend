package com.budget.app.controller;

import com.budget.app.entity.User;
import com.budget.app.jwt.service.JwtService;
import com.budget.app.model.Response;
import com.budget.app.model.user.LoginRequest;
import com.budget.app.model.user.LoginResponse;
import com.budget.app.model.user.UserInfoOnLogin;
import com.budget.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody User user) throws Exception {

        User newUser = user;
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Response response = new Response();

        try {
            userService.registerUser(user);

            response.setMessage("User has been successfully registered");
            response.setStatusCode(HttpStatus.OK.value());
            response.setTimestamp(LocalDateTime.now());

        } catch (Exception e) {
            throw e;
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {

        LoginResponse loginResponse;

        // authenticate the user with the user provided email and password
        try {

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // get user details using email provided
            UserDetails userDetails = userDetailsService
                                            .loadUserByUsername(loginRequest.getEmail());

            // generating token from the user retrieved
            String token = jwtService.generateToken(userDetails);

            User user = userService.findUserByEmail(userDetails.getUsername()).get();

            loginResponse = new LoginResponse();
            loginResponse.setAuthToken(token);

            UserInfoOnLogin userInfo = new UserInfoOnLogin(user.getId(), user.getFirstName(), user.getRole());

            loginResponse.setUser(userInfo);

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/who")
    public ResponseEntity<LoginResponse> checkUser(@RequestHeader("authorization") String header) throws Exception {

        LoginResponse loginResponse;

        try {
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

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }

    }

}
