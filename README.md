# Stock Price Generator Microservice

## Overview

The Stock Price Generator Microservice is a robust Spring Boot application designed to simulate real-time stock price updates. It uses a hybrid architecture that combines:
- **Event Streaming:** Publishes stock price updates to a Kafka topic (using KRaft mode, without Zookeeper).
- **Persistent Storage:** Saves and updates stock price records in a PostgreSQL database.

In addition, the service automatically generates stock prices at scheduled intervals and also exposes REST endpoints for manual interaction and data retrieval.

## Features

- **Real-Time Data Streaming:**  
  Publishes stock price updates to a Kafka topic for real-time processing.

- **Database Persistence:**  
  Persists stock price records in PostgreSQL for historical analysis and auditing.

- **Scheduled Price Generation:**  
  Uses Spring's scheduling support to automatically generate and update stock prices at configurable intervals.

- **REST API:**  
  Exposes endpoints for:
  - Publishing stock price updates
  - Retrieving stock prices by symbol

- **Hybrid Approach:**  
  Combines the benefits of event streaming with persistent storage to ensure that no data is lost and real-time consumers receive immediate updates.

- **Containerized Environment:**  
  Easily run required services (Kafka in KRaft mode and PostgreSQL) via Docker Compose.

## Prerequisites

- **Java 17** or later
- **Gradle** (or use the provided Gradle wrapper)
- **Docker Desktop** (for running PostgreSQL and Kafka containers)
- Basic knowledge of Spring Boot, Kafka, and PostgreSQL is beneficial

## Project Structure

```plaintext
stock-price-generator/
├── docker-compose.yml           # Docker Compose configuration for PostgreSQL and Kafka (KRaft mode)
├── README.md                    # Project documentation
├── build.gradle                 # Gradle build file with all dependencies
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── stockpricegenerator/
│   │   │           ├── StockPriceGeneratorApplication.java   # Main Spring Boot application class
│   │   │           ├── controller/
│   │   │           │   └── StockPriceController.java          # REST API controller
│   │   │           ├── model/
│   │   │           │   └── StockPrice.java                    # JPA entity representing stock price records
│   │   │           ├── repository/
│   │   │           │   └── StockPriceRepository.java          # Spring Data repository for StockPrice
│   │   │           ├── service/
│   │   │           │   └── StockPriceService.java             # Service layer for business logic and Kafka publishing
│   │   │           └── scheduler/
│   │   │               └── StockPriceScheduler.java           # Scheduled job for automatic stock price generation
│   │   └── resources/
│   │       └── application.yml                              # Application configuration (PostgreSQL, Kafka, etc.)
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── stockpricegenerator/
        │           ├── StockPriceGeneratorApplicationTests.java  # Basic Spring context tests
        │           ├── controller/
        │           │   └── StockPriceControllerTest.java           # Unit tests for REST endpoints
        │           ├── service/
        │           │   └── StockPriceServiceTest.java              # Unit tests for service layer
        │           └── scheduler/
        │               └── StockPriceSchedulerTest.java            # Unit tests for scheduled tasks
        └── resources/
            └── application-test.yml                             # Test configuration using H2 in-memory database

---

## Contact

**Dzmitry Ivaniuta** — [diafter@gmail.com] — [GitHub: https://github.com/DimitryIvaniuta]
