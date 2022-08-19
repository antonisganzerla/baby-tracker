package com.sgztech.service;

import com.sgztech.domain.entity.User;
import com.sgztech.rest.dto.CreateUserDTO;
import com.sgztech.rest.dto.UserDTO;
import com.sgztech.rest.dto.CredentialsDTO;

public interface UserService extends EntityService<User, UserDTO> {
    UserDTO save(CreateUserDTO dto);
    void auth(CredentialsDTO dto);
}