package com.example.demo.service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList() // No roles for now
        );
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<Users> authenticate(String username, String rawPassword) {
        Optional<Users> userOpt = userRepository.findByUsername(username);
        return userOpt.filter(user -> encoder.matches(rawPassword, user.getPassword()));
    }

    public Optional<Users> findByUsername(String username) {
        return Optional.of(userRepository.findByUsername(username).orElseThrow());
    }
}
