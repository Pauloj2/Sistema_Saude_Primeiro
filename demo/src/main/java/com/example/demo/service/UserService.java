package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {

    Long save(User user);

    User findByEmail(String email);
}