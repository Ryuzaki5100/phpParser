FROM maven:3.9.9-eclipse-temurin-17 AS java-build
WORKDIR /app
COPY demo/pom.xml .
COPY demo/src ./src
COPY demo/.mvn ./.mvn
COPY demo/mvnw .
COPY demo/mvnw.cmd .
RUN chmod +x /app/mvnw
RUN ./mvnw clean install -e -X
RUN ls -la target/

FROM php:8.3.19-cli
WORKDIR /app

RUN apt-get update && apt-get install -y \
    openjdk-17-jre \
    libzip-dev \
    unzip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

COPY demo/php /app/php
WORKDIR /app/php

COPY demo/php/composer.json demo/php/composer.lock* ./
RUN composer install --no-dev --optimize-autoloader

COPY demo/php ./

COPY --from=java-build /app/target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV PHP_PATH="/app/php"

CMD ["java", "-jar", "/app/app.jar"]