package com.sgztech.service;

import com.sgztech.domain.entity.Register;
import com.sgztech.domain.entity.User;
import com.sgztech.domain.repository.RegisterRepository;
import com.sgztech.domain.repository.UserRepository;
import com.sgztech.exception.EntityNotFoundException;
import com.sgztech.rest.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RegisterDTO save(RegisterDTO dto) {
        userRepository
                .findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Register register = repository.save(mapToRegister(dto));
        return mapToRegisterDto(register);
    }

    @Override
    public RegisterDTO getById(Integer id) {
        Register register = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
        return mapToRegisterDto(register);
    }

    @Override
    public void update(Integer id, RegisterDTO dto) {
        repository.findById(id)
                .map(b -> {
                    dto.setId(b.getId());
                    dto.setUserId(b.getUser().getId());
                    repository.save(mapToRegister(dto));
                    return b;
                }).orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id)
                .map(b -> {
                    repository.delete(b);
                    return Void.TYPE;
                }).orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
    }

    @Override
    public List<RegisterDTO> find(RegisterDTO filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAny()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Register register = mapToRegister(filter);
        Example<Register> example = Example.of(register, matcher);
        return repository.findAll(example)
                .stream().map(b -> mapToRegisterDto(b))
                .collect(Collectors.toList());
    }

    private Register mapToRegister(RegisterDTO dto) {
        Register register = new Register();
        register.setId(dto.getId());
        register.setName(dto.getName());
        register.setIcon(dto.getIcon());
        register.setDescription(dto.getDescription());
        register.setLocalDateTime(dto.getLocalDateTime());
        register.setDuration(dto.getDuration());
        register.setNote(dto.getNote());
        register.setType(dto.getType());
        register.setSubType(dto.getSubType());
        register.setRegistrationDate(LocalDateTime.now());
        User user = new User();
        user.setId(dto.getUserId());
        register.setUser(user);
        return register;
    }

    private RegisterDTO mapToRegisterDto(Register register) {
        RegisterDTO dto = new RegisterDTO();
        dto.setId(register.getId());
        dto.setName(register.getName());
        dto.setIcon(register.getIcon());
        dto.setDescription(register.getDescription());
        dto.setLocalDateTime(register.getLocalDateTime());
        dto.setDuration(register.getDuration());
        dto.setNote(register.getNote());
        dto.setType(register.getType());
        dto.setSubType(register.getSubType());
        dto.setUserId(register.getUser().getId());
        return dto;
    }
}
