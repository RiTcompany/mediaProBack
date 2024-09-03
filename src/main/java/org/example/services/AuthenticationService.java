package org.example.services;

import org.example.pojo.*;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    void sendValidationMsgToEmail(String email);

    void validateEmail(String email, Long pin);

    Boolean doesUserAlreadyExist(String email);

    Long changePassword(ChangePasswordRequest changePasswordRequest);

    void forgotPassword(String email);

    Long changeForgottenPassword(ForgotPasswordRequest forgotPasswordRequest);
}
