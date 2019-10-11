package com.rds.gdpr.patterns.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebServerVerticle extends AbstractVerticle {

    private HttpServer server;

    public void start(Promise future) {


        OpenAPI3RouterFactory.create(this.vertx, "webroot/swagger/chat.json", openAPI3RouterFactoryAsyncResult -> {

            if (openAPI3RouterFactoryAsyncResult.failed()) {
                Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
                log.error("oops, something went wrong during factory initialization", exception);
                future.fail(exception);
            }

            OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result()
                    .mountServicesFromExtensions();

            routerFactory.addFailureHandlerByOperationId("createUser", routingContext -> {
                log.error("Exception", routingContext.failure());
            });

            Router router = routerFactory.setOptions(new RouterFactoryOptions()
                    .setMountResponseContentTypeHandler(true))
                    .getRouter();


            router.mountSubRouter("/eventbus", SockJSHandler.create(vertx)
                    .bridge(new BridgeOptions()
                            .addInboundPermitted(new PermittedOptions()
                                    .setAddress("chat-service-inbound"))
                            .addOutboundPermitted(new PermittedOptions()
                                    .setAddress("chat-service-outbound"))));

            router.route().handler(StaticHandler.create("webroot"));

            server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"))
                    .requestHandler(router).listen((ar) -> {
                        if (ar.succeeded()) {
                            log.info("Server started on port {}", ar.result().actualPort());
                            future.complete();
                        } else {
                            log.error("oops, something went wrong during server initialization", ar.cause());
                            future.fail(ar.cause());
                        }
                    });
        });

    }

    public void stop() {
        this.server.close();
    }

}
