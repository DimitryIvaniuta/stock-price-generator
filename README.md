# Stock Price Generator Microservice

This project is a hybrid microservice built with Spring Boot, Kafka (using KRaft mode), and PostgreSQL. It demonstrates how to generate, update, and publish stock price updates in real time while persisting the data for historical analysis.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running the Project](#running-the-project)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Docker Compose](#docker-compose)
- [License](#license)

## Features
- **Real-time Updates:** Publish stock price updates to a Kafka topic.
- **Database Persistence:** Store and update stock prices in PostgreSQL.
- **REST API:** Expose endpoints to publish and retrieve stock prices.
- **Scheduled Generation:** Automatically generate stock price updates at regular intervals.
- **Dockerized Environment:** Run Kafka (in KRaft mode) and PostgreSQL via Docker Compose.

## Prerequisites
- Java 17 or later
- Gradle (or use the Gradle Wrapper)
- Docker Desktop (for Windows, macOS, or Linux)
- Internet connection (to pull Docker images)

## Project Structure
```stock-price-generator/
├── docker-compose.yml
├── README.md
├── build.gradle
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── stockpricegenerator/
│   │   │           ├── StockPriceGeneratorApplication.java
│   │   │           ├── controller/
│   │   │           │   └── StockPriceController.java
│   │   │           ├── model/
│   │   │           │   └── StockPrice.java
│   │   │           ├── repository/
│   │   │           │   └── StockPriceRepository.java
│   │   │           ├── service/
│   │   │           │   └── StockPriceService.java
│   │   │           └── scheduler/
│   │   │               └── StockPriceScheduler.java
│   │   └── resources/
│   │       └── application.yml
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── stockpricegenerator/
        │           ├── StockPriceGeneratorApplicationTests.java
        │           ├── controller/
        │           │   └── StockPriceControllerTest.java
        │           ├── service/
        │           │   └── StockPriceServiceTest.java
        │           └── scheduler/
        │               └── StockPriceSchedulerTest.java
        └── resources/
            └── application-test.yml

```


## Configuration
### Application Configuration (`application.yml`)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/stockdb
    username: stockuser
    password: stockpass
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect

  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
