package com.myroom.authserver.data.mapper.firebase;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.myroom.authserver.api.model.UserResponseModel;
import com.myroom.authserver.data.dto.User;
import com.myroom.authserver.data.model.FirebaseUserModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FirebaseMapper {
    public User firebaseDecodedTokenToUserDto(FirebaseToken decodedToken){
        User user = null;
        if(decodedToken != null){
            user = new User();
            user.setUid(decodedToken.getUid());
            user.setName(decodedToken.getName());
            user.setEmail(decodedToken.getEmail());
            user.setPicture(decodedToken.getPicture());
            user.setIssuer(decodedToken.getIssuer());
            user.setEmailVerified(decodedToken.isEmailVerified());
        }
        return user;
    }

    public UserResponseModel toUserResponseModel(UserRecord userRecord) {
        return UserResponseModel.builder()
                .uid(userRecord.getUid())
                .email(userRecord.getEmail())
                .name(userRecord.getDisplayName())
                .isEmailVerified(userRecord.isEmailVerified())
                .providerId(userRecord.getProviderId())
                .picture(userRecord.getPhotoUrl())
                .build();
    }

    public Optional<FirebaseUserModel> toUserModel(UserRecord userRecord) {
        FirebaseUserModel user = FirebaseUserModel.builder()
                .uid(userRecord.getUid())
                .name(userRecord.getDisplayName())
                .email(userRecord.getEmail())
                .isEmailVerified(userRecord.isEmailVerified())
                .build();

        return Optional.of(user);
    }
}
