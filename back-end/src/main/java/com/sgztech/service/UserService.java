package com.sgztech.service;

import com.sgztech.domain.entity.User;
import com.sgztech.rest.dto.CreateUserDTO;
import com.sgztech.rest.dto.UserDTO;
import com.sgztech.rest.dto.CredentialsDTO;
import com.sgztech.rest.dto.UserTokenDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends EntityService<User, UserDTO>, UserDetailsService {
    UserDTO save(CreateUserDTO dto);
    UserTokenDTO auth(CredentialsDTO dto);
}