package com.rds.gdpr.patterns.service;

import com.rds.gdpr.patterns.dto.ChatMessageDto;
import com.rds.gdpr.patterns.dto.UserDto;
import com.rds.gdpr.patterns.model.User;
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

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final EventBus eventBus;

    @Override
    public void getAllUsers(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        log.info("Context: {}", context.toJson());
        usersRepository.findAll(find ->
                resultHandler.handle(find.map(all -> OperationResponse.completedWithJson(
                        Json.encodeToBuffer(all
                                .stream()
                                .peek(entries -> log.info("User: {}", entries))
                                .map(entries -> UserDto.of(entries))
                                .collect(Collectors.toList()))))));
    }

    @Override
    public void createUser(JsonObject body, OperationRequest context, Handler<AsyncResult<OperationResponse>> handler) {
        log.info("Context: {}", context.toJson());
        log.info("Body: {}", body.encodePrettily());
        usersRepository.save(User.builder().name(body.mapTo(UserDto.class).getName()).build(), save ->
                handler.handle(save.map(id -> OperationResponse.completedWithPlainText(Buffer.buffer(id)).setStatusCode(201))
                        .otherwise(throwable -> OperationResponse.completedWithPlainText(Buffer.buffer(throwable.getMessage()))
                                .setStatusCode(500))));
    }

    @Override
    public void getUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> handler) {
        log.info("Context: {}", context.toJson());
        log.info("Id: {}", id);
        usersRepository.findById(id, find ->
                handler.handle(find
                        .map(user -> user
                                .map(model -> OperationResponse.completedWithJson(Json.encodeToBuffer(UserDto.of(model))))
                                .orElse(OperationResponse.completedWithPlainText(Buffer.buffer("Not Found")).setStatusCode(404)))
                        .otherwise(throwable -> OperationResponse.completedWithPlainText(Buffer.buffer(throwable.getMessage()))
                                .setStatusCode(500))));
    }

    @Override
    public void deleteUser(String id, OperationRequest context, Handler<AsyncResult<OperationResponse>> handler) {
        log.info("Context: {}", context.toJson());
        usersRepository.delete(id, delete ->
                handler.handle(delete
                        .map(count -> count > 0 ?
                                new OperationResponse().setStatusCode(204).setStatusMessage("OK") :
                                new OperationResponse().setStatusCode(404).setStatusMessage("Not found"))
                        .otherwise(throwable -> new OperationResponse().setStatusCode(404).setStatusMessage("Not found"))));
    }

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
