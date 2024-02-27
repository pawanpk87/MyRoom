package com.myroom.authserver.usecase.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.myroom.authserver.api.model.UserResponseModel;
import com.myroom.authserver.data.dto.User;
import com.myroom.authserver.data.mapper.firebase.FirebaseMapper;
import com.myroom.authserver.data.model.FirebaseUserModel;
import com.myroom.authserver.exception.AuthServiceFirebaseInvalidTokenException;
import com.myroom.authserver.usecase.UserService;
import com.myroom.authserver.usecase.impl.firebase.FirebaseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    FirebaseMapper firebaseMapper;

    @Autowired
    FirebaseAuthService firebaseAuthService;

    @Override
    public User getUserByFirebaseDecodedToken(FirebaseToken firebaseDecodedToken) {
        return firebaseMapper.firebaseDecodedTokenToUserDto(firebaseDecodedToken);
    }

    @Override
    public Optional<UserResponseModel> getUserByUID(String uid){
        Optional<UserResponseModel> userResponse = Optional.empty();
        try {
            UserRecord userRecord =  FirebaseAuth.getInstance().getUser(uid);
            userResponse = Optional.ofNullable(firebaseMapper.toUserResponseModel(userRecord));
        }catch (FirebaseAuthException e) {
            throw new AuthServiceFirebaseInvalidTokenException(e.getAuthErrorCode(), e.getMessage());
        }
        return userResponse;
    }

    @Override
    public Optional<FirebaseUserModel> getUserByBearerToken(String bearerToken) {
        UserRecord userRecord = firebaseAuthService.getUserByBearerToken(bearerToken);
        Optional<FirebaseUserModel> user = firebaseMapper.toUserModel(userRecord);
        return user;
    }
}
