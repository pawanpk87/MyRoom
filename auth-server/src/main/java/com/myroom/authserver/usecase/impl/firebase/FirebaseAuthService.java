package com.myroom.authserver.usecase.impl.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.myroom.authserver.data.dto.Credentials;
import com.myroom.authserver.data.dto.SecurityProperties;
import com.myroom.authserver.exception.AuthServiceFirebaseInvalidTokenException;
import com.myroom.authserver.utils.firebase.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {
    @Autowired
    SecurityService securityService;

    @Autowired
    SecurityProperties securityProperties;

    @Autowired
    CookieUtil cookieUtil;

    public void verifyToken(HttpServletRequest request) {
        String session = null;
        FirebaseToken decodedToken = null;
        Credentials.CredentialType type = null;
        boolean strictServerSessionEnabled = securityProperties.getFirebaseProps().isEnableStrictServerSession();
        Cookie sessionCookie = cookieUtil.getCookie("session");
        String bearerToken = securityService.getBearerToken(request);
        try {
            if (sessionCookie != null){
                session = sessionCookie.getValue();
                /**
                 * Verify the session cookie. In this case an additional check is added to detect
                 * if the user's Firebase session was revoked, user deleted/disabled, etc.
                 * */
                final boolean checkRevoked = securityProperties.getFirebaseProps().isEnableCheckSessionRevoked();
                decodedToken = FirebaseAuth.getInstance().verifySessionCookie(session, checkRevoked);
            }else if (!strictServerSessionEnabled){
                decodedToken = FirebaseAuth.getInstance().verifyIdToken(bearerToken);
                type = Credentials.CredentialType.ID_TOKEN;
            }
        } catch (FirebaseAuthException e) {
            throw new AuthServiceFirebaseInvalidTokenException(e.getAuthErrorCode(), e.getMessage());
        }
    }

    public UserRecord getUserByBearerToken(String bearerToken){
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(bearerToken);
            return FirebaseAuth.getInstance().getUser(decodedToken.getUid());
        }catch (FirebaseAuthException e) {
            throw new AuthServiceFirebaseInvalidTokenException(e.getAuthErrorCode(), e.getMessage());
        }
    }
}
