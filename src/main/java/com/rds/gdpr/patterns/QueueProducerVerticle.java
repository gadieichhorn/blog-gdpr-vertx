package com.rds.gdpr.patterns;

import com.rds.gdpr.patterns.service.QueueProducerService;
import com.rds.gdpr.patterns.service.QueueProducerServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;

public class QueueProducerVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        new ServiceBinder(this.vertx)
                .setAddress("queue-producer.proxy")
                .register(QueueProducerService.class, new QueueProducerServiceImpl(this.vertx));
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        // TODO cleanup
        stopPromise.complete();
    }

}
