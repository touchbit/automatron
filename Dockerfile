# https://hub.docker.com/r/azul/zulu-openjdk-alpine
FROM azul/zulu-openjdk-alpine:17

WORKDIR /app
COPY ./target/automatron.jar /app/
EXPOSE 8080 9092
ENTRYPOINT ["java", "-jar", "/app/automatron.jar"]
