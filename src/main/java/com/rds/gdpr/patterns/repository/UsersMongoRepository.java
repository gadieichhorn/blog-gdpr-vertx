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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UsersMongoRepository implements UsersRepository {

    private static final String COLLECTION = "users";

    private final MongoClient client;

    @Override
    public void findById(String id, Handler<AsyncResult<Optional<User>>> handler) {
        log.debug("ID: {}", id);
        client.findOne(COLLECTION, new JsonObject().put("_id", id), null, find ->
                handler.handle(find
                        .map(json -> Optional.ofNullable(json).map(model -> model.mapTo(User.class)))));
    }

    @Override
    public void findAll(Handler<AsyncResult<List<User>>> handler) {
        client.find(COLLECTION, new JsonObject(), all ->
                handler.handle(all
                        .map(jsonObjects -> jsonObjects.stream()
                                .map(entries -> entries.mapTo(User.class))
                                .collect(Collectors.toList()))));
    }

    @Override
    public void save(User user, Handler<AsyncResult<String>> handler) {
        log.info("User: {}", user);
        client.insert(COLLECTION, JsonObject.mapFrom(user), handler);
    }

    @Override
    public void delete(String id, Handler<AsyncResult<Long>> handler) {
        log.info("Id: {}", id);
        client.removeDocument(COLLECTION, new JsonObject().put("_id", id), event ->
                handler.handle(event
                        .map(delete -> delete.getRemovedCount())));
    }

}
