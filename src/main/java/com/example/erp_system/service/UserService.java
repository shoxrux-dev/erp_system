package com.example.erp_system.service;


import com.example.erp_system.dto.user.UpdatePasswordOfUserRequestDto;
import com.example.erp_system.dto.user.UpdateUsernameOfUserRequestDto;
import com.example.erp_system.exception.RecordNotFoundException;
import com.example.erp_system.model.Role;
import com.example.erp_system.model.User;
import com.example.erp_system.repository.RoleRepository;
import com.example.erp_system.repository.UserRepository;
import com.example.erp_system.validation.CommonSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommonSchemaValidator commonSchemaValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User save(User user) {
        commonSchemaValidator.userNotExist(user.getUsername());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Instant now = Instant.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setPassword(hashedPassword);
        user.setImage("avatar.png");

        Role role = Role.builder().name("ADMIN").build();
        roleRepository.save(role);

        user.setRoles(List.of(role));

        return userRepository.save(user);
    }

    public boolean updatePassword(UpdatePasswordOfUserRequestDto updatePasswordOfUserRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User byUsername = getByUsername(authentication.getName());
        boolean matches = passwordEncoder.matches(updatePasswordOfUserRequestDto.getCurrentPassword(), byUsername.getPassword());
        if(matches) {
            String hashedPassword = passwordEncoder.encode(updatePasswordOfUserRequestDto.getNewPassword());
            byUsername.setPassword(hashedPassword);
            userRepository.save(byUsername);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUsername(UpdateUsernameOfUserRequestDto updateUsernameOfUserRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User byUsername = getByUsername(authentication.getName());
        byUsername.setUsername(updateUsernameOfUserRequestDto.getNewUsername());
        try {
            userRepository.save(byUsername);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private User getByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found with %s username", username)));
    }

}
