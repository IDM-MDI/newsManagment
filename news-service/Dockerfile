FROM openjdk:17-jdk-alpine
EXPOSE 8080
ENV PROFILE = dev
ENV CACHE_TYPE = lfu
ENV CACHE_SIZE = 10
COPY build/libs/news-service-1.0.0.jar news-1.0.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}","-jar","news-1.0.jar"]