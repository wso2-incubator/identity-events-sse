# Smaple POC for Shared Signals and Events(SSE) Specification 

REST API based on Java Spring Boot that demonstrates how the [Shared Signals and Events (SSE)](https://openid.net/specs/openid-sse-framework-1_0-ID1.html) Framework works.

## Prerequisites

* JDK 1.8
* Maven 3.x
* MongoDB 5.0.6

## Getting started

1. Clone the project and run it using a code editor ( Intellij, Eclipse)
    * The project will start running in port 8080
2. Run the [WSO2 Identity server](https://github.com/wso2/product-is#installation-and-running) 
3. Deploy a sample application in Tomcat
     * The sample application should be a [configured as Service Provider in Wso2 Identity Server](https://is.docs.wso2.com/en/latest/learn/adding-and-configuring-a-service-provider/)
     
4. Run MongoDB database 
    *  Start MongoDB by running the following command in MongDB bin folder path.
    ```
    $ mongod
    ```
   * You might need to update the default MongoDB URI `spring.data.mongodb.uri` in the `application.properties` file to your port number where MongoDB is running.

## Swagger 

View the SwaggerHub:
```
https://app.swaggerhub.com/apis-docs/randiepathirage/Sample-SSE/1.0.0
```
View the Swagger UI open your browser to here:
```
http://localhost:8080/swagger-ui/
```
The Swagger API documentation 2.0 is at:
```
http://localhost:8080/v2/api-docs
```

## Endpoints

### Transmitter configuration 
Retrieve transmitter Configuration information. 
```
curl --location --request GET 'http://localhost:8080/well-known/sse-configuration'
```

### Management API

#### Configuration Endpoint 
Receivers can read, update and delete stream configuration. 
```
curl --location --request GET 'http://localhost:8080/well-known/sse-configuration'
```
```
curl --location --request DELETE 'http://localhost:8080/sse/stream'
```
```
curl --location --request POST 'http://localhost:8080/sse/stream' \
--header 'Content-Type: application/json' \
--data-raw '{
  "iss": "https://tr.example.com",
  "aud": [
    "http://receiver.example.com/web",
    "http://receiver.example.com/mobile"
  ],
  "delivery": [
      "https://schemas.openid.net/secevent/risc/delivery-method/push",
      "https://schemas.openid.net/secevent/risc/delivery-method/push"
  ],
  "events_requested": [
    "urn:example:secevent:events:type_100",
    "urn:example:secevent:events:type_3",
    "urn:example:secevent:events:type_4"
  ]
}'
```

#### Status Endpoint 
Receivers can read stream status, update steam status.
```
curl -X GET 'http://localhost:8080/sse/status'
```
```
curl --location --request POST 'http://localhost:8080/sse/status' \
--header 'Content-Type: application/json' \
--data-raw '{
    "status": "enabled"
}'
```

#### Add Subject Endpoint
```
curl --location --request POST 'http://localhost:8080/sse/subjects:add' \
--header 'Content-Type: application/json' \
--data-raw '{
    "format":"email",
    "email":"tom@gmail.com"
}'
```
#### Remove Subject Endpoint
```
curl --location --request POST 'http://localhost:8080/sse/subjects:remove' \
--header 'Content-Type: application/json' \
--data-raw '{
    "format":"email",
    "email":"afffv@gmail.com"
}'
```

#### Verification Endpoint 
Sends verification Event over the Event Stream to check whether the stream is working properly.
```
curl --location --request POST 'http://localhost:8080/sse/verify'
```
### Event Control
Store events in database and send event to subscribers.

 */event endpoint is not part of the SSE spec*

```
curl --location --request POST 'http://localhost:8080/event' \
--header 'Content-Type: application/json' \
--data-raw '{
    "token":"exampleToken"
}'
```

