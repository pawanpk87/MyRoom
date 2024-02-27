package com.myroom.authserver.usecase.impl.firebase;

import com.myroom.authserver.data.dto.Credentials;
import com.myroom.authserver.data.dto.SecurityProperties;
import com.myroom.authserver.data.dto.User;
import com.myroom.authserver.utils.firebase.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    SecurityProperties securityProperties;

    public User getUser(){
        User userPrincipal = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        if(principal instanceof  User){
            userPrincipal = ((User) principal);
        }
        return userPrincipal;
    }

    public Credentials getCredentials(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return ((Credentials) securityContext.getAuthentication().getCredentials());
    }

    public boolean isPublic(){
        return securityProperties.getAllowedPublishApis().contains(httpServletRequest.getRequestURI());
    }

    public String getBearerToken(HttpServletRequest request){
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if(authorization != null &&  authorization.startsWith("Bearer ")){
            bearerToken = authorization.substring(7);
        }
        return bearerToken;
    }
}
