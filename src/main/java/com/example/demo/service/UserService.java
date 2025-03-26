package com.example.demo.service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Task;
import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public abstract class UserService implements UserDetails{

    private final Users user;

    public UserService(Users user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Autowired
    private UserRepository userRepository;


    public Users saveUser(Users users)
    {
        return userRepository.save(users);
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public abstract UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public Optional<Users> findByUsername(String username) {
        return Optional.of(userRepository.findByUsername(username).orElseThrow());
    }
}
