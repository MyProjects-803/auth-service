package com.job.service;

import com.job.config.JwtUtil;
import com.job.entity.RoleEntity;
import com.job.entity.UserEntity;
import com.job.exception.InvalidCredentialException;
import com.job.exception.RoleNotFoundException;
import com.job.exception.UserAlreadyExistException;
import com.job.repo.RoleRepo;
import com.job.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(String username, String password, String roleName) {

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException("Username already exists");
        }

        RoleEntity role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    public String login(String username, String password) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialException("Invalid credentials");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();

        return jwtUtil.generateToken(username, roles);
    }
}