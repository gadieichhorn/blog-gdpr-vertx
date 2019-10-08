package com.rds.gdpr.patterns.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;

@WebApiServiceGen
public interface UsersService {

    void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void postUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void updateUser(String id, JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void getUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void deleteUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}