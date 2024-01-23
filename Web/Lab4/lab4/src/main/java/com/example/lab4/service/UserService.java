package com.example.lab4.service;

import com.example.lab4.model.User;
import com.example.lab4.entity.UserEntity;
import com.example.lab4.repository.UserRepo;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@ToString
@EqualsAndHashCode
@Service
public class UserService implements UserDetailsService {

    private final UserRepo repository;

    @Autowired
    public UserService(UserRepo repository) {
        this.repository = repository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity foundUser = repository.findByUsername(username);

        if (foundUser == null)
            throw new UsernameNotFoundException("Cannot find user with name " + username);

        return User.toModel(foundUser);
    }
}
