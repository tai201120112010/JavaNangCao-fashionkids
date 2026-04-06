package com.example.fashionkids.service;

import com.example.fashionkids.entity.User;
import com.example.fashionkids.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User save(User user) {
        return repo.save(user);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }
}