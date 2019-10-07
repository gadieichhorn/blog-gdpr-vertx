package com.rds.gdpr.patterns.repository;

import com.rds.gdpr.patterns.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

}
