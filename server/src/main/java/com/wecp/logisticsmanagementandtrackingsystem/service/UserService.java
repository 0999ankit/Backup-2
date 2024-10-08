package com.wecp.logisticsmanagementandtrackingsystem.service;


import com.wecp.logisticsmanagementandtrackingsystem.entity.User;
import com.wecp.logisticsmanagementandtrackingsystem.exception.UserExistsException;
import com.wecp.logisticsmanagementandtrackingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserExistsException("User alreay exists with the email "+user.getEmail() + " Please try some other credentials.");
        }else if (userRepository.existsByUsername(user.getUsername())){
            throw new UserExistsException("User alreay exists with the username "+user.getUsername() + " Please try some other credentials.");
        }
        else{
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
        }
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}