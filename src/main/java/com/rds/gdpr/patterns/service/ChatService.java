package com.rds.gdpr.patterns.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;

@ProxyGen
@VertxGen
public interface ChatService {

    String ADDRESS = "chat.proxy";

    void createUserMessage(String id, String body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}