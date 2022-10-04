package com.sgztech.service;

import com.sgztech.domain.entity.User;
import com.sgztech.rest.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends EntityService<User, UserDTO>, UserDetailsService {
    UserDTO save(CreateUserDTO dto);
    UserTokenDTO auth(CredentialsDTO dto);

    ForgotPasswordDTO forgotPassword(ForgotPasswordDTO dto);

    ForgotPasswordDTO verificationCode(ForgotPasswordCodeDTO dto);

    ForgotPasswordDTO resetPassword(ResetPasswordDTO dto);
}