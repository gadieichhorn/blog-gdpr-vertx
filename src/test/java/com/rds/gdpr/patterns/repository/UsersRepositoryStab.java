package com.rds.gdpr.patterns.repository;

import com.rds.gdpr.patterns.model.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UsersRepositoryStab implements UsersRepository {

    private final Map<String, User> users = new HashMap<>();

    public UsersRepositoryStab(List<User> data) {
        data.stream().forEach(user -> users.put(user.getId(), user));
        log.info("Users: {}", users);
    }

    @Override
    public void findAll(Handler<AsyncResult<List<User>>> handler) {

    }

    @Override
    public void findById(String id, Handler<AsyncResult<Optional<User>>> handler) {
        log.info("ID: {}", id);
        handler.handle(Future.succeededFuture(Optional.of(users.get(id))));
    }

    @Override
    public void save(User user, Handler<AsyncResult<String>> handler) {

    }

    @Override
    public void delete(String id, Handler<AsyncResult<Long>> handler) {

    }

}
