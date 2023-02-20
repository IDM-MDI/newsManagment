FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY build/libs/newsManagment-1.0.0.jar news-1.0.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar","news-1.0.jar"]