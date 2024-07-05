# Dockerfile
FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Añadir netcat para verificar la disponibilidad de MySQL
RUN apt-get update && apt-get install -y netcat

# Script para esperar a que MySQL esté listo
RUN echo '#!/bin/sh' > /wait-for-mysql.sh && \
    echo 'while ! nc -z mysql 3306; do sleep 1; done' >> /wait-for-mysql.sh && \
    echo 'java -jar /app.jar' >> /wait-for-mysql.sh && \
    chmod +x /wait-for-mysql.sh

ENTRYPOINT ["/wait-for-mysql.sh"]