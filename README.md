# Quota Management with Spring Boot

This project is a quota management application built with Spring Boot and Java 17.

## Technologies Used

- Java 17
- Spring Boot
- MySQL
- Gradle
- Docker
- JUnit
- JaCoCo (for code coverage)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed Docker Desktop. You can download it from [Docker's official website](https://www.docker.com/products/docker-desktop).

## How to Run

The following steps will guide you on how to run this project:

1. Clean the project:
    ```bash
    ./gradlew clean
    ```

2. Build the project:
    ```bash
    ./gradlew build
    ```

3. Build the Docker image:
    ```bash
    docker build -t my-java-app .
    ```

4. Run the Docker containers:
    ```bash
    docker-compose up -d
    ```

This will start three Docker containers: one for the MySQL database, one for the Adminer (a web interface for managing the MySQL database), and one for the Java application.

## Testing

You can find a Postman collection in the project files named `Vicarius.postman_collection.json` for testing the application's endpoints. To start testing the application, begin with the "Create user" endpoint in the provided Postman collection. Once a user is created, you can proceed to test the other endpoints.

## Configuration

This application has a feature flag for the following properties:

- `app.mysql-time.start` and `app.mysql-time.end`: These properties define the start and end times for when MySQL is used.
- `quota.request.limit`: This property defines the limit for quotas.
- `scheduling.resetQuotasTime`: This property defines the time or day when quotas are reset. By default, quotas are reset once a day at 1 PM.

You can change these properties in the `application.yml` file to suit your needs.