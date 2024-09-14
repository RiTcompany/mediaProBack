package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.User;
import org.example.enums.ERole;
import org.example.exceptions.UserUsernameAlreadyExistsException;
import org.example.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements org.example.services.UserService {
    private final UserRepository repository;


    @Override
    public User save(User user) {
        return repository.save(user);
    }



    @Override
    public User create(User user) {
        if (repository.existsByUsername(user.getEmail())) {
            throw new UserUsernameAlreadyExistsException(user.getEmail());
        }
        return save(user);
    }


    @Override
    public User getByUsername(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));
    }


    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    @Override
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(ERole.ROLE_STANDARD);
        save(user);
    }
}