# News Management System
![Clevertec-logo](https://sortlist-core-api.s3.eu-west-1.amazonaws.com/anq21gq3yg16iedrippbqixybqkb)

---
The application was created for implementing functionality for working with the news management system.

## Used technologies
* *Java 17*
* *Gradle*
* *Spring Boot 3*
* *Spring Data JPA*
* *Spring Security*
* *Spring AOP*
* *Spring Cloud API Gateway*
* *Spring Cloud Eureka*
* *Spring Cloud Config*
* *JWT*
* *Liquibase*
* *Protobuf*
* *PostgreSQL*
* *JUnit 5*
* *Mockito*
* *TestContainers*
* *Logback*
* *Lombok*
* *Swagger*
* *Docker*

## Modules
### Api-Gateway
```
API Gateway is a fully managed service for developers,
designed to create, publish, maintain, monitor and
ensure API security at any scale. Via Application API
access data, business logic, or functionality
your backend services. API Gateway allows you to create RESTful APIs and
WebSocket, which are the main component of applications for two-way
communications in real time. API Gateway Supports Workloads
in containers and serverless workloads, as well as web applications.

API Gateway takes care of all the tasks associated with receiving and processing hundreds of
thousands of concurrent API calls, including traffic management, CORS support,
authorization and access control, regulation of the number of requests, monitoring
and API versioning. Working with API Gateway does not require minimum payments
or start-up investment. You pay for API calls received and volume transferred
data and can use API Gateway's tiered pricing model
lower your costs as your API usage scales.

URL: http://localhost:8765
```

### Config Server
```
Spring Cloud Config is Spring's client/server approach for storing and serving 
distributed configurations across multiple applications and environments.

This configuration store is ideally versioned under Git version control and 
can be modified at application runtime. While it fits very well in Spring 
applications using all the supported configuration file formats together with 
constructs like Environment, PropertySource, or @Value, it can be used in any 
environment running any programming language.
Configs are stored in resources 'config' folder
```

### Exception-Service
```
This is a service that catches exceptions thrown in our application and 
view them in REST. It is not a microservice.
```

### Log-Service
```
This is a service that listens to all layers of the application and logs them 
using logback. It is not a microservice.
```

### News-Service
```
This is a microservice that performs CRUD operations with news and their comments.
Secured: CRD operations
URL: http://{api-gateway}/news
```

### Server
```
Eureka is the Netflix Service Discovery Server and Client. The server can be 
configured and deployed to be highly available, with each server replicating state about the registered services to the others.
```

### User-Service
```
This is a microservice that allows you to login, register, or validate a JWT Token.
URL: http://{api-gateway}/users
```

## How to run
* Install java 17 and Gradle
* Install docker
* Change profile(default: dev) in .env file
* Execute *docker_start.sh* in root project

## Endpoints
About endpoints and api, you can introduce by running the application and following to link
```
http://{api-gateway}/{service}/swagger-ui.html
```

## Entities

### Users
The users table stores information about registered users on the application. 
Each user has a unique *username*, a *password* for authentication, 
and a *role* which determines their level of access on the app.

Roles: 

*Subscriber*(can comment news, and crud operation for only own comments), 

*Journalist*(can comment and crud news for only own too), 

*Admin* (can crud all operation)
### News
The news table stores information about news articles on the application. 
Each article has a unique *id*, a *title*, the *text* of the article, 
the *username* of the user who posted it, 
and a *created_date* timestamp indicating when the article was published.

### Comments
The comments table stores information about comments made on news articles. 
Each comment has a unique *id*, the *text* of the comment, 
the *username* of the user who made it, the *news_id* of the article it was made on, 
and a *created_date* timestamp indicating when the comment was posted.

The *news_id* and *username* columns in the comments table both have foreign key constraints 
referencing the id and username columns in the news and users tables, respectively.

## DTO
The DTO is generated using a proto that is located in the directory
```
{root}/src/main/proto/DTO.proto
```