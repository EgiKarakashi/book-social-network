FROM openjdk:17-jdk-alpine

EXPOSE 8099

ARG JAR_FILE=book-network/target/book-network-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT exec java -jar /app.jar