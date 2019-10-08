package com.rds.gdpr.patterns.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface QueueProducerService {

    void create(String topic, JsonObject body, Handler<AsyncResult<Void>> resultHandler);

}