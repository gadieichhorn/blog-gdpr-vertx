package com.rds.gdpr.patterns.service;

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

@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final MongoClient client;

    @Override
    public void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        client.find("users", new JsonObject(), res -> {
            if (res.succeeded()) {
                resultHandler.handle(Future.succeededFuture(
                        OperationResponse.completedWithJson(Json.encodeToBuffer(res.result()))
                ));
            } else {
                log.error("Failed to get all Document(s) (User)", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public void postUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context);
        JsonObject document = new JsonObject()
                .put("name", body.getValue("name"));
        client.insert("users", document, res -> {
            if (res.succeeded()) {
                String id = res.result();
                log.info("Inserted book with id {}", id);
                resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(document)));
            } else {
                log.error("Failed to insert a Document (User)", res.cause());
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
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
