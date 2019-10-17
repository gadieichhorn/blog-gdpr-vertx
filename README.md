
# GDPR Chat

This is a Demo application to demonstrate encryption of user senstative data (like chat conversations with support operator) into Kafka queues.

## Design

Users are created using a REST API (See Swagger)
> http://localhost:8080/private/swagger/

Users holds a unique RSA encryption pair per user. This is used to encrypt messages per user.

When a user is removed from the system or the private key is deleted the messages stored in the persistent queue are not accessible.

This represent the right to be forgotten GDPR requirement.

The main Chat page 
> http://localhost:8080/private/chat.html

Place a test message and see the message that was saved in Kafak and the message after decryption

Check the log to see the inner process of message flow through the system.

## Cipher
The encryption and decryption of messages is performed in two steps.

The RSA key pair is generated per user and stored in the user profile in MongoDB

### Step 1

Messages encrypted using DES Symetric key. this is because the RSA encryption is limited by content size and can only do messages based on the key size. DES Symteric is not limited.
The encrypted message is saved into the message body

### Step 2

The symmetric key (used in step 1) is encrypted using the user public key. The encrypted symmetric key is saved into the message.
Messages is pushed to Kafka queue.

## Wish to be forgotten

When a user private key is removed, any messages in the queue encrypted with the user key are not accessible anymore. 
Because each message is encrypted with a different symmetric key, even if one message key was discovered none of the other messages are jeopardised.

## Technologies

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

# Building

## To launch your tests:
```
./mvnw clean test
```

## To package your application:
```
./mvnw clean package
```

## To run your application:

start docker services
```bash
docker-compose up
```
start the application
```
java -jar target/patterns-0.0.1-SNAPSHOT-fat.jar 

```
login using the main chat page
> http://localhost:8080/private/chat.html

# Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]
