package com.rds.gdpr.patterns.repository;

import com.rds.gdpr.patterns.model.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UsersRepository {

    private final MongoClient client;

    public void findById(String id, Handler<AsyncResult<Optional<User>>> handler) {
        log.debug("ID: {}", id);
        client.findOne(User.COLLECTION, new JsonObject().put("_id", id), null, find -> {
            if (find.succeeded()) {
                if (find.result() == null) {
                    log.warn("No results found for ID: {}", id);
                    handler.handle(Future.succeededFuture(Optional.empty()));
                } else {
                    handler.handle(Future.succeededFuture(Optional.ofNullable(find.result().mapTo(User.class))));
                }
            } else {
                log.error("Failed to load user", find.cause());
                handler.handle(Future.failedFuture(find.cause()));
            }
        });
    }

    public void findAll(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        client.find(User.COLLECTION, new JsonObject(), resultHandler);
    }

    public void save(User user, Handler<AsyncResult<String>> handler) {
        log.info("User: {}", user);
        client.insert(User.COLLECTION, JsonObject.mapFrom(user), handler);
    }

}
