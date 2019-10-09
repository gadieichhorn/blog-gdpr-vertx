package com.rds.gdpr.patterns.service;

import com.rds.gdpr.patterns.dto.UserDto;
import com.rds.gdpr.patterns.model.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final MongoClient client;

    @Override
    public void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        client.find("users", new JsonObject(), find -> {
            if (find.succeeded()) {
                resultHandler.handle(Future.succeededFuture(
                        OperationResponse.completedWithJson(Json.encodeToBuffer(find.result().stream()
                                .peek(entries -> log.info("User: {}", entries.encodePrettily()))
                                .map(entries -> entries.mapTo(User.class))
                                .collect(Collectors.toList())
                        ))
                ));
            } else {
                log.error("Failed to get all Document(s) (User)", find.cause());
                resultHandler.handle(Future.failedFuture(find.cause()));
            }
        });
    }

    @Override
    public void createUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", body.encodePrettily());
        client.insert("users", User.of(body.mapTo(UserDto.class)), insert -> {
            if (insert.succeeded()) {
                log.info("Inserted Document (User) with id {}", insert.result());
                resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithPlainText(Buffer.buffer(insert.result()))));
            } else {
                log.error("Failed to insert a Document (User)", insert.cause());
                resultHandler.handle(Future.failedFuture(insert.cause()));
            }
        });
    }

    @Override
    public void getUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        log.info("Context: {}", id);
        client.findOne("users", new JsonObject(), null, findOne -> {
            if (findOne.succeeded()) {
                if (findOne.result() == null) {
                    resultHandler.handle(Future.succeededFuture(
                            OperationResponse.completedWithPlainText(Buffer.buffer("Not found"))
                                    .setStatusCode(404)));
                } else {
                    resultHandler.handle(Future.succeededFuture(
                            OperationResponse.completedWithJson(Json.encodeToBuffer(findOne.result().mapTo(User.class)))));
                }
            } else {
                log.error("Failed to get all Document(s) (User)", findOne.cause());
                resultHandler.handle(Future.failedFuture(findOne.cause()));
            }
        });
    }

    @Override
    public void updateUser(String id, JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        log.info("Context: {} : {}", id, body);
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithPlainText(Buffer.buffer("Hello User!"))
        ));
    }

    @Override
    public void deleteUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithPlainText(Buffer.buffer("Hello User!"))
        ));
    }

}
