package com.sgztech.service;

import com.sgztech.domain.entity.User;
import com.sgztech.domain.repository.UserRepository;
import com.sgztech.exception.AuthException;
import com.sgztech.exception.BusinessRuleException;
import com.sgztech.exception.EntityNotFoundException;
import com.sgztech.rest.dto.CreateUserDTO;
import com.sgztech.rest.dto.CredentialsDTO;
import com.sgztech.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDTO save(CreateUserDTO dto) {
        if (isPasswordsMatch(dto))
            throw new BusinessRuleException("{field.password.and.confirm.password.do.not.match}");

        if (repository.findByEmail(dto.getEmail()).isPresent())
            throw new BusinessRuleException("{email.is.already.in.use}");

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRegistrationDate(LocalDateTime.now());
        return mapToUserDTO(save(user));
    }

    private boolean isPasswordsMatch(CreateUserDTO dto) {
        return !dto.getPassword().equals(dto.getConfirmPassword());
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public UserDTO getById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("{user.not.found}"));
        return mapToUserDTO(user);
    }

    @Override
    public void update(Integer id, User user) {
        repository.findById(id)
                .map(u -> {
                    user.setId(u.getId());
                    repository.save(user);
                    return Void.TYPE;
                }).orElseThrow(() -> new EntityNotFoundException("{user.not.found}"));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id)
                .map(u -> {
                    repository.delete(u);
                    return Void.TYPE;
                }).orElseThrow(() -> new EntityNotFoundException("{user.not.found}"));
    }

    @Override
    public List<User> find(User filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<User> example = Example.of(filter, matcher);
        return repository.findAll(example);
    }

    @Override
    public void auth(CredentialsDTO dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthException("{user.or.password.invalid}"));

        if (!isPasswordMatch(dto, user)) {
            throw new AuthException("{user.or.password.invalid}");
        }
    }

    private boolean isPasswordMatch(CredentialsDTO dto, User user) {
        String encryptedPassword = dto.getPassword();
        return user.getPassword().equals(encryptedPassword);
    }
}
