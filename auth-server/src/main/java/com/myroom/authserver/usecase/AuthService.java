package com.myroom.authserver.usecase;

import com.myroom.authserver.api.model.VerifyTokenResponseModel;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    VerifyTokenResponseModel verifyToken(HttpServletRequest httpServletRequest);
}
