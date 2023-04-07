FROM openjdk:17-jdk-alpine
EXPOSE 8080
ENV PROFILE = dev
COPY build/libs/user-service-1.0.0.jar users-1.0.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}","-jar","users-1.0.jar"]