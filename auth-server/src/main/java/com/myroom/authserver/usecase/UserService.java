package com.myroom.authserver.usecase;

import com.google.firebase.auth.FirebaseToken;
import com.myroom.authserver.api.model.UserResponseModel;
import com.myroom.authserver.data.dto.User;
import com.myroom.authserver.data.model.FirebaseUserModel;

import java.util.Optional;

public interface UserService {
    User getUserByFirebaseDecodedToken(FirebaseToken firebaseDecodedToken);

    Optional<UserResponseModel> getUserByUID(String uid);

    Optional<FirebaseUserModel> getUserByBearerToken(String bearerToken);
}
