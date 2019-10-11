package com.rds.gdpr.patterns.repository;

import com.rds.gdpr.patterns.model.User;
import io.vertx.core.AsyncResult;
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
        client.findOne(User.COLLECTION, new JsonObject().put("_id", id), null, find ->
                handler.handle(find.map(json -> Optional.ofNullable(json).map(model -> model.mapTo(User.class)))));
    }

    public void findAll(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        client.find(User.COLLECTION, new JsonObject(), resultHandler);
    }

    public void save(User user, Handler<AsyncResult<String>> handler) {
        log.info("User: {}", user);
        client.insert(User.COLLECTION, JsonObject.mapFrom(user), handler);
    }

    public void delete(String id, Handler<AsyncResult<Long>> handler) {
        log.info("Id: {}", id);
        client.removeDocument(User.COLLECTION, new JsonObject().put("_id", id), event ->
                handler.handle(event.map(delete -> delete.getRemovedCount())));
    }

}
