package com.example.jobhunter.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetailsService;

@Component("userDetailsService")
public class UserDetailsCustomer implements UserDetailsService {
    private final UserService userService;

    public UserDetailsCustomer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.example.jobhunter.domain.User user = this.userService.handleGetUserByEmail(username);
        if(user == null)
            throw new UsernameNotFoundException("Invalid Username/Password");
        return new User(user.getEmail(), user.getPassWord(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    }
}
