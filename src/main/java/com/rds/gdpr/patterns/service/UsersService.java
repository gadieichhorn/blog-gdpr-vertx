package com.rds.gdpr.patterns.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;

@WebApiServiceGen
public interface UsersService {

    String ADDRESS = "users.proxy";

    void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void createUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void getUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void deleteUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void createUserMessage(String id, String body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}