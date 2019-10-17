package com.rds.gdpr.patterns.service;

import com.rds.gdpr.patterns.dto.ChatMessageDto;
import com.rds.gdpr.patterns.repository.UsersRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UsersRepository usersRepository;
    private final EventBus eventBus;

    @Override
    public void createUserMessage(String id, String body, OperationRequest context, Handler<AsyncResult<OperationResponse>> handler) {
        log.info("Context: {}", context.toJson());
        log.info("Id: {}", id);
        log.info("Body: {}", body);
        usersRepository.findById(id, find ->
                handler.handle(find
                        .map(user -> user
                                .map(model -> {
                                    eventBus.publish("chat-service-inbound", Json.encode(ChatMessageDto.builder()
                                            .from(id)
                                            .message(body)
                                            .build()));
                                    return OperationResponse.completedWithJson(Json.encodeToBuffer(new JsonObject()));
                                })
                                .orElse(OperationResponse.completedWithPlainText(Buffer.buffer("Not Found")).setStatusCode(404)))
                        .otherwise(throwable -> OperationResponse.completedWithPlainText(Buffer.buffer(throwable.getMessage()))
                                .setStatusCode(500))));
    }

}
