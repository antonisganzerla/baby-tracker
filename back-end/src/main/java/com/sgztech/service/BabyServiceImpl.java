package com.sgztech.service;

import com.sgztech.domain.entity.Baby;
import com.sgztech.domain.entity.User;
import com.sgztech.domain.repository.BabyRepository;
import com.sgztech.domain.repository.UserRepository;
import com.sgztech.exception.EntityNotFoundException;
import com.sgztech.rest.dto.BabyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BabyServiceImpl implements BabyService {

    @Autowired
    private BabyRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BabyDTO save(BabyDTO dto) {
        userRepository
                .findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Baby baby = repository.save(mapToBaby(dto));
        return mapToBabyDto(baby);
    }

    @Override
    public BabyDTO getById(Integer id) {
        Baby baby = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bebê não encontrado"));
        return mapToBabyDto(baby);
    }

    @Override
    public void update(Integer id, BabyDTO dto) {
        repository.findById(id)
                .map(b -> {
                    dto.setId(b.getId());
                    dto.setUserId(b.getUser().getId());
                    repository.save(mapToBaby(dto));
                    return b;
                }).orElseThrow(() -> new EntityNotFoundException("Bebê não encontrado"));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id)
                .map(b -> {
                    repository.delete(b);
                    return Void.TYPE;
                }).orElseThrow(() -> new EntityNotFoundException("Bebê não encontrado"));
    }

    @Override
    public List<BabyDTO> find(BabyDTO babyFilter) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAny()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Baby baby = mapToBaby(babyFilter);
        Example<Baby> example = Example.of(baby, matcher);
        return repository.findAll(example)
                .stream().map(b -> mapToBabyDto(b))
                .collect(Collectors.toList());
    }

    private Baby mapToBaby(BabyDTO dto) {
        Baby baby = new Baby();
        baby.setId(dto.getId());
        baby.setName(dto.getName());
        baby.setBirthday(dto.getBirthday());
        baby.setSex(dto.getSex());
        baby.setPhotoUri(dto.getPhotoUri());
        baby.setRegistrationDate(LocalDateTime.now());
        User user = new User();
        user.setId(dto.getUserId());
        baby.setUser(user);
        return baby;
    }

    private BabyDTO mapToBabyDto(Baby baby) {
        BabyDTO dto = new BabyDTO();
        dto.setId(baby.getId());
        dto.setName(baby.getName());
        dto.setBirthday(baby.getBirthday());
        dto.setSex(baby.getSex());
        dto.setPhotoUri(baby.getPhotoUri());
        dto.setUserId(baby.getUser().getId());
        return dto;
    }
}