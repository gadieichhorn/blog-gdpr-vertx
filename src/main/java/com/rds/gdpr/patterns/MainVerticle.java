package com.rds.gdpr.patterns;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.RequestParameter;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.validation.ValidationException;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class MainVerticle extends AbstractVerticle {

    private HttpServer server;


    public void start(Promise future) {
        // Load the api spec. This operation is asynchronous
        OpenAPI3RouterFactory.create(this.vertx, "chat.yml", openAPI3RouterFactoryAsyncResult -> {

            if (openAPI3RouterFactoryAsyncResult.failed()) {
                // Something went wrong during router factory initialization
                Throwable exception = openAPI3RouterFactoryAsyncResult.cause();
                log.error("oops, something went wrong during factory initialization", exception);
                future.fail(exception);
            }
            // Spec loaded with success
            OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();
            // Add an handler with operationId
            routerFactory.addHandlerByOperationId("listPets", routingContext -> {
                // Load the parsed parameters
                RequestParameters params = routingContext.get("parsedParameters");
                // Handle listPets operation
                RequestParameter limitParameter = params.queryParameter(/* Parameter name */ "limit");
                if (limitParameter != null) {
                    // limit parameter exists, use it!
                    Integer limit = limitParameter.getInteger();
                } else {
                    // limit parameter doesn't exist (it's not required).
                    // If it's required you don't have to check if it's null!
                }
                routingContext.response()
                        .setStatusMessage("OK")
                        .end(Json.encode(ChatMessage.builder().key(UUID.randomUUID()).message("ssss").build()));
            });
            // Add a failure handler
            routerFactory.addFailureHandlerByOperationId("listPets", routingContext -> {
                // This is the failure handler
                Throwable failure = routingContext.failure();
                if (failure instanceof ValidationException)
                    // Handle Validation Exception
                    routingContext.response()
                            .setStatusCode(400)
                            .setStatusMessage("ValidationException thrown! " + ((ValidationException) failure).type().name())
                            .end();
            });

            // Add a security handler
            routerFactory.addSecurityHandler("api_key", routingContext -> {
                // Handle security here
                routingContext.next();
            });

            // Before router creation you can enable/disable various router factory behaviours
            RouterFactoryOptions factoryOptions = new RouterFactoryOptions()
//                    .setMountValidationFailureHandler(false) // Disable mounting of dedicated validation failure handler
                    .setMountResponseContentTypeHandler(true); // Mount ResponseContentTypeHandler automatically

            // Now you have to generate the router
            Router router = routerFactory.setOptions(factoryOptions).getRouter();

            // Allow events for the designated addresses in/out of the event bus bridge
//            BridgeOptions opts = new BridgeOptions()
//                    .addInboundPermitted(new PermittedOptions().setAddress("chat.to.server"))
//                    .addOutboundPermitted(new PermittedOptions().setAddress("chat.to.client"));

            // Create the event bus bridge and add it to the router.
//            router.route("/eventbus/*").handler(SockJSHandler.create(vertx)
//                    .bridge(new BridgeOptions()
//                    .addInboundPermitted(new PermittedOptions())));

            // Create a router endpoint for the static content.
            router.route().handler(StaticHandler.create("webroot"));

            EventBus eb = vertx.eventBus();

            // Register to listen for messages coming IN to the server
            eb.consumer("chat.to.server").handler(message -> {
                // Create a timestamp string
                String timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Date.from(Instant.now()));
                // Send the message back out to all clients with the timestamp prepended.
                eb.publish("chat.to.client", timestamp + ": " + message.body());
            });

            // Now you can use your Router instance
            server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
            server.requestHandler(router).listen((ar) -> {
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
