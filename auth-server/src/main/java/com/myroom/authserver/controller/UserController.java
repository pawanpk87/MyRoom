package com.myroom.authserver.controller;

import com.myroom.authserver.api.model.UserResponseModel;
import com.myroom.authserver.api.resource.UserResource;
import com.myroom.authserver.usecase.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@Slf4j
public class UserController implements UserResource {
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<UserResponseModel> getUser(String uid) {
        log.info("Getting the user details for: {}", uid);
        Optional<UserResponseModel> userResponse = userService.getUserByUID(uid);
        if(userResponse.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Fetched user details is: {}",userResponse);
        return new ResponseEntity<>(userResponse.get(), HttpStatus.OK);
    }
}
