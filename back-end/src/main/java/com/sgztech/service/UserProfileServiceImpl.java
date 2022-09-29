package com.sgztech.service;

import com.sgztech.domain.entity.UserProfile;
import com.sgztech.domain.repository.UserProfileRepository;
import com.sgztech.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository repository;

    @Override
    public UserProfile save(UserProfile entity) {
        UserProfile userProfile = new UserProfile();
        userProfile.setName(entity.getName());
        userProfile.setRegistrationDate(LocalDateTime.now());
        return repository.save(userProfile);
    }

    @Override
    public UserProfile getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("{user.profile.not.found}"));
    }

    @Override
    public void update(Integer id, UserProfile entity) {
        repository.findById(id)
                .map(u -> {
                    entity.setId(u.getId());
                    repository.save(entity);
                    return Void.TYPE;
                }).orElseThrow(() -> new EntityNotFoundException("{user.profile.not.found}"));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id)
                .map(u -> {
                    repository.delete(u);
                    return Void.TYPE;
                }).orElseThrow(() -> new EntityNotFoundException("{user.profile.not.found}"));
    }

    @Override
    public List<UserProfile> find(UserProfile filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matchingAny()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<UserProfile> example = Example.of(filter, matcher);
        return repository.findAll(example);
    }
}
