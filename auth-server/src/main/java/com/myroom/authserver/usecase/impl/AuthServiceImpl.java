package com.myroom.authserver.usecase.impl;

import com.myroom.authserver.api.constants.AuthStatus;
import com.myroom.authserver.api.model.VerifyTokenResponseModel;
import com.myroom.authserver.data.mapper.firebase.FirebaseMapper;
import com.myroom.authserver.usecase.AuthService;
import com.myroom.authserver.usecase.impl.firebase.FirebaseAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    FirebaseAuthService firebaseAuthService;
    @Autowired
    FirebaseMapper firebaseMapper;

    @Override
    public VerifyTokenResponseModel verifyToken(HttpServletRequest httpServletRequest) {
        firebaseAuthService.verifyToken(httpServletRequest);
        VerifyTokenResponseModel verifyTokenResponse = new VerifyTokenResponseModel();
        verifyTokenResponse.setStatus(AuthStatus.SUCCESS);
        verifyTokenResponse.setMessage("Token successfully verified");
        return verifyTokenResponse;
    }
}
