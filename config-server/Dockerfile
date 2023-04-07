FROM openjdk:17-jdk-alpine
EXPOSE 8888
COPY build/libs/config-server-1.0.0.jar app.jar
ENV GIT_CONFIG_URI = https://github.com/IDM-MDI/news-management-config

ENTRYPOINT ["java","-jar","app.jar"]