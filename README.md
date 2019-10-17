= Starter

image:https://img.shields.io/badge/vert.x-3.8.2-purple.svg[link="https://vertx.io"]

This application was generated using http://start.vertx.io

== Building

To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```

== Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]


== GDPR Chat

This is a Demo application to demonstrate encryption of user senstative data (like chat conversations with support operator) into Kafka queues.

=== Design

Users are created using a REST API (See Swagger)
> http://localhost:8080/private/swagger/

Users holds a unique RSA encryption pair per user. This is used to encrypt messages per user.

When a user is removed from the system or the private key is deleted the messages stored in the persistent queue are not accessible.

This represent the right to be forgotten GDPR requirement.

The main Chat page 
> http://localhost:8080/private/chat.html

Place a test message and see the message that was saved in Kafak and the message after decryption

Check the log to see the inner process of message flow through the system.

=== Technologies

Ths demo uses few technologies to accomplish the workflow

* Eclipse Vertx.io
    * Web API / OpenAPI 3
    * Swagger
    * Service Proxy
    * Shiro auth 
    * ThymeLeafe
    * SockJS
    * Apache Kafka Client
    * MongoDB Client
* Java Cipher
* Docker/Compose
    * Zookeeper
    * Apache Kafka
    * MongoDB
* JUnit 5
    * Flapdoodle (mongo test)
    * Vertx Extention
    * javaFaker
