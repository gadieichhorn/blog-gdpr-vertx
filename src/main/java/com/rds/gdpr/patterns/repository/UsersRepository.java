package com.rds.gdpr.patterns.repository;

import com.rds.gdpr.patterns.model.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {

    void findById(String id, Handler<AsyncResult<Optional<User>>> handler);

    void findByName(String name, Handler<AsyncResult<Optional<User>>> handler);

    void findAll(Handler<AsyncResult<List<User>>> handler);

    void save(User user, Handler<AsyncResult<String>> handler);

    void delete(String id, Handler<AsyncResult<Long>> handler);

}
