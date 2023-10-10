package com.example.erp_system.service;

import com.example.erp_system.model.User;
import com.example.erp_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUserEntity = userRepository.findUserByUsername(username);
        return optionalUserEntity.orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found", username)));
    }

}
