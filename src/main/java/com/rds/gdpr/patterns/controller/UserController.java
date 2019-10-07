package com.rds.gdpr.patterns.controller;

import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    private Mono<User> getById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping
    private Flux<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping()
    private Mono<User> crate(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> detete(@PathVariable Long id) {
        return userRepository.deleteById(id);
    }

}
