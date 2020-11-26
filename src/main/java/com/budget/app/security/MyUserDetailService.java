package com.budget.app.security;

import com.budget.app.entity.User;
import com.budget.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    // finding user by its email and setting its details
    // if not found, then throwing UsernameNotFoundException
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userService.findUserByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException("No such user found"));

        return user.map(MyUserDetails::new).get();
    }
}
