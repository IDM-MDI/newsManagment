FROM openjdk:17-jdk-alpine
EXPOSE 8080
ENV PROFILE = prod
COPY build/libs/newsManagment-1.0.0.jar news-1.0.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}","-jar","news-1.0.jar"]