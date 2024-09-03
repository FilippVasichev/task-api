FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY build/libs/task-api-1.0.jar /app/task-api.jar

EXPOSE 8080

CMD ["sh", "-c", "java ${ARGS} -jar task-api.jar"]