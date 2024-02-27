package com.myroom.authserver.usecase.impl;

import com.myroom.authserver.data.model.FirebaseUserModel;
import com.myroom.authserver.data.model.UserDetailsImpl;
import com.myroom.authserver.usecase.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;


    @Override
    public UserDetailsImpl loadUserByUsername(String bearerToken) throws UsernameNotFoundException {
        FirebaseUserModel user = userService.getUserByBearerToken(bearerToken).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        return UserDetailsImpl.builder()
                .email(user.getEmail())
                .password(user.getUid())
                .disabled(user.isDisabled())
                .build();
    }
}
