package com.budget.app.jwt.filter;

import com.budget.app.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // getting authorization from request header
        final String authorizationHeader = httpServletRequest.getHeader("authorization");

        String username = null;
        String token = null;

        // checking if authorization header is not null
        // and starts with "Bearer"
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // extracting token from header
            token = authorizationHeader.substring(7);

            // extracting username from token;
            username = jwtService.extractUsername(token);

        }

        // checking if username is not null
        // and there is no security context
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // getting user details from username(in this case, email)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // checking if token is valid
            if(jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }

        // passing request to next chain
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
