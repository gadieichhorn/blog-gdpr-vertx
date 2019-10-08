package com.rds.gdpr.patterns.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final Vertx vertx;

    @Override
    public void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithJson(Json.encodeToBuffer(User.builder()
                        .id(UUID.randomUUID().toString())
                        .name(UUID.randomUUID().toString())
                        .build()))
        ));
    }

    @Override
    public void postUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithPlainText(Buffer.buffer("Hello User!"))
        ));
    }

    @Override
    public void getUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithPlainText(Buffer.buffer("Hello User!"))
        ));
    }

    @Override
    public void updateUser(String transactionId, JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithPlainText(Buffer.buffer("Hello User!"))
        ));
    }

    @Override
    public void deleteUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        resultHandler.handle(Future.succeededFuture(
                OperationResponse.completedWithPlainText(Buffer.buffer("Hello User!"))
        ));
    }

}
