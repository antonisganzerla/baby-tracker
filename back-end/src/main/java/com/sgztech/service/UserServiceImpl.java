package com.sgztech.service;

import com.sgztech.domain.dto.EmailDetailsDTO;
import com.sgztech.domain.entity.PasswordRecovery;
import com.sgztech.domain.entity.User;
import com.sgztech.domain.entity.UserProfile;
import com.sgztech.domain.repository.PasswordRecoveryRepository;
import com.sgztech.domain.repository.UserProfileRepository;
import com.sgztech.domain.repository.UserRepository;
import com.sgztech.exception.AuthException;
import com.sgztech.exception.BusinessRuleException;
import com.sgztech.exception.EntityNotFoundException;
import com.sgztech.rest.dto.*;
import com.sgztech.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDTO save(CreateUserDTO dto) {
        if (isPasswordsMatch(dto))
            throw new BusinessRuleException("{field.password.and.confirm.password.do.not.match}");

        if (repository.findByEmail(dto.getEmail().toLowerCase()).isPresent())
            throw new BusinessRuleException("{email.is.already.in.use}");

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());

        UserProfile userProfile = userProfileRepository.findByName(UserProfile.USER)
                .orElseThrow(() -> new BusinessRuleException("{user.profile.not.found}"));
        user.setUserProfile(userProfile);
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
    public UserTokenDTO auth(CredentialsDTO dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthException("Usuário ou senha incorretos"));

        if (!isPasswordMatch(dto, user)) {
            throw new AuthException("Usuário ou senha incorretos");
        }

        UserTokenDTO userToken = new UserTokenDTO();
        userToken.setId(user.getId());
        userToken.setName(user.getName());
        userToken.setEmail(user.getEmail());
        userToken.setToken(jwtService.generateToken(user));
        return userToken;
    }

    @Override
    public String forgotPassword(ForgotPasswordDTO dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("{user.not.found}"));

        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setUser(user);
        passwordRecovery.setExpiration(LocalDateTime.now().plusHours(1));
        passwordRecovery.setCode(getRandomNumberUsingInts(1000, 9999).toString());
        passwordRecovery.setEmail(dto.getEmail());
        passwordRecovery.setUsed(false);

        EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO();
        emailDetailsDTO.setSubject("Recuperação de senha");
        emailDetailsDTO.setMsgBody("Seu código de recuperação de senha: " + passwordRecovery.getCode());
        emailDetailsDTO.setRecipient(dto.getEmail());
        emailService.sendSimpleMail(emailDetailsDTO);

        passwordRecoveryRepository.save(passwordRecovery);

        return dto.getEmail();
    }

    private Integer getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    @Override
    public String verificationCode(ForgotPasswordCodeDTO dto) {
        PasswordRecovery passwordRecovery = passwordRecoveryRepository.findByEmailAndCodeAndIsUsed(dto.getEmail(), dto.getCode(), false)
                .orElseThrow(() -> new BusinessRuleException("Código inválido"));

        if (passwordRecovery.getExpiration().isBefore(LocalDateTime.now()))
            throw new BusinessRuleException("Tempo expirado");

        return dto.getEmail();
    }

    @Override
    public String resetPassword(ResetPasswordDTO dto) {
        PasswordRecovery passwordRecovery = passwordRecoveryRepository.findByEmailAndCodeAndIsUsed(dto.getEmail(), dto.getCode(), false)
                .orElseThrow(() -> new BusinessRuleException("Código inválido"));

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("{user.not.found}"));

        if (!dto.getPassword().equals(dto.getConfirmPassword()))
            throw new BusinessRuleException("{field.password.and.confirm.password.do.not.match}");

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        repository.save(user);

        passwordRecovery.setUsed(true);
        passwordRecoveryRepository.save(passwordRecovery);

        return dto.getEmail();
    }

    private boolean isPasswordMatch(CredentialsDTO dto, User user) {
        return passwordEncoder.matches(dto.getPassword(), user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("{user.not.found}"));

        String[] roles = user.getUserProfile().getName().equals(UserProfile.ADMIN) ?
                new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
