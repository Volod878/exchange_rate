FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=build/libs/exchange_rate-1.0.0-SNAPSHOT.jar
WORKDIR /libs//
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
