FROM openjdk:17-jdk-alpine
EXPOSE 8888
COPY spring-config/news-management root/spring-config/news-management
COPY build/libs/config-server-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]