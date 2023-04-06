FROM openjdk:17-jdk-alpine
EXPOSE 8761
COPY build/libs/server-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]