FROM openjdk:17-jdk-alpine
EXPOSE 8765
COPY build/libs/api-getaway-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]