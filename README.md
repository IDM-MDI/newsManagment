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
* *JWT*
* *Flyway*
* *Protobuf*
* *PostgreSQL*
* *H2Database*
* *JUnit 5*
* *Mockito*
* *Lombok*
* *Swagger*
* *Docker*

## How to run
* Install docker
* Change profile(default: prod)
* Execute *docker_start.sh* in root project

## Endpoints
About endpoints and api, you can introduce by running the application and following to link
```
http://localhost:8080/swagger-ui.html
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