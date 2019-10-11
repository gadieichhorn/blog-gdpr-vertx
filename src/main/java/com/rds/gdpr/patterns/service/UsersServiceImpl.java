package com.rds.gdpr.patterns.service;

import com.rds.gdpr.patterns.dto.UserDto;
import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UsersRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        usersRepository.findAll(all -> {
            if (all.succeeded()) {
                resultHandler.handle(Future.succeededFuture(
                        OperationResponse.completedWithJson(Json.encodeToBuffer(all.result().stream()
                                .peek(entries -> log.info("User: {}", entries.encodePrettily()))
                                .map(entries -> entries.mapTo(User.class))
                                .collect(Collectors.toList())
                        ))
                ));
            } else {
                log.error("Failed to get all Document(s) (User)", all.cause());
                resultHandler.handle(Future.failedFuture(all.cause()));
            }
        });
    }

    @Override
    public void createUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        log.info("Body: {}", body.encodePrettily());
        usersRepository.save(User.builder().name(body.mapTo(UserDto.class).getName()).build(), save -> {
            if (save.succeeded()) {
                log.info("Save Document (User) with id {}", save.result());
                resultHandler.handle(Future.succeededFuture(OperationResponse
                        .completedWithPlainText(Buffer.buffer(save.result()))
                        .setStatusCode(201)));
            } else {
                log.error("Failed to save a Document (User)", save.cause());
                resultHandler.handle(Future.failedFuture(save.cause()));
            }
        });
    }

    @Override
    public void getUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        log.info("Id: {}", id);
        usersRepository
                .findById(id, find -> find
                        .map(user -> Future.succeededFuture(user
                                .map(model -> OperationResponse.completedWithJson(Json.encodeToBuffer(UserDto.of(model))))
                                .orElse(OperationResponse.completedWithPlainText(Buffer.buffer("Not Found")).setStatusCode(404))))
                        .otherwise(throwable -> Future.failedFuture(throwable)));
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
