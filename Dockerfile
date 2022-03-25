# https://hub.docker.com/r/azul/zulu-openjdk-alpine
FROM openjdk:17
# OS/ARCH - windows/amd64, linux/amd64, linux/arm64/v8

WORKDIR /app
COPY ./target/automatron.jar /app/
EXPOSE 8080 9092
ENTRYPOINT ["java", "-jar", "/app/automatron.jar"]
