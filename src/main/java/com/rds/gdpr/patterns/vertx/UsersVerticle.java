package com.rds.gdpr.patterns.vertx;

import com.rds.gdpr.patterns.model.User;
import com.rds.gdpr.patterns.repository.UsersMongoRepository;
import com.rds.gdpr.patterns.repository.UsersRepository;
import com.rds.gdpr.patterns.service.UsersService;
import com.rds.gdpr.patterns.service.UsersServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ServiceBinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsersVerticle extends AbstractVerticle {

    private MongoClient mongoClient;
    private UsersRepository usersRepository;
    private ServiceBinder serviceBinder;
    private MessageConsumer<JsonObject> consumer;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        log.info("Config: {}", context.config());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        mongoClient = MongoClient.createShared(vertx, config().getJsonObject("mongo"));
        usersRepository = new UsersMongoRepository(mongoClient);
        serviceBinder = new ServiceBinder(this.vertx);

        consumer = serviceBinder
                .setAddress(UsersService.ADDRESS)
                .register(UsersService.class, new UsersServiceImpl(usersRepository, vertx.eventBus()));

        usersRepository.findByName("tim", find -> {
            if (find.succeeded()) {
                if (find.result().isPresent()) {
                    startPromise.complete();
                } else {
                    usersRepository.save(User.builder().name("tim").build(), saved -> startPromise.complete());
                }
            } else {
                startPromise.fail(find.cause());
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        consumer.unregister();
        mongoClient.close();
        stopPromise.complete();
    }

}
