# Stock Price Generator Microservice

## Overview

The Stock Price Generator Microservice is a comprehensive Spring Boot application designed to simulate real-time stock price updates. This microservice demonstrates a hybrid approach by combining event streaming with persistent storage. Stock prices are generated automatically at scheduled intervals and are both stored in a PostgreSQL database and published to a Kafka topic (running in KRaft mode). This architecture enables real-time processing as well as historical data analysis.

## Features

- **Real-Time Stock Price Updates:**  
  Automatically generates and publishes simulated stock prices at regular intervals.

- **Hybrid Data Management:**  
  Persists stock prices in PostgreSQL for historical analysis while simultaneously streaming updates to Kafka for real-time processing.

- **Scheduled Price Generation:**  
  Utilizes Spring's scheduling capabilities to trigger stock price updates at a configurable interval.

- **RESTful Integration:**  
  Provides REST endpoints (e.g., for manual triggering or data retrieval) to interact with the service.

- **In-Memory Testing Support:**  
  Includes test configuration that leverages an H2 in-memory database for fast and isolated integration tests.

- **Containerized Environment:**  
  Designed to work with Docker (using Docker Compose) to easily run required services like Kafka (in KRaft mode) and PostgreSQL locally.

## Prerequisites

- **Java 17** (or later)
- **Gradle** (or the provided Gradle wrapper)
- **Docker Desktop** (to run the PostgreSQL and Kafka containers)
- Basic knowledge of Spring Boot, Kafka, and PostgreSQL is helpful

## Project Structure

```plaintext
stock-price-generator/
├── docker-compose.yml
├── README.md
├── build.gradle
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── stockpricegenerator/
│   │   │           ├── StockPriceGeneratorApplication.java         // Main application class
│   │   │           ├── controller/
│   │   │           │   └── StockPriceController.java                // REST API controller for manual interactions
│   │   │           ├── model/
│   │   │           │   └── StockPrice.java                          // JPA entity for stock prices
│   │   │           ├── repository/
│   │   │           │   └── StockPriceRepository.java                // Spring Data repository for stock prices
│   │   │           ├── service/
│   │   │           │   └── StockPriceService.java                   // Business logic for saving, updating, and publishing prices
│   │   │           └── scheduler/
│   │   │               └── StockPriceScheduler.java                 // Scheduled job for generating prices automatically
│   │   └── resources/
│   │       └── application.yml                                      // Application configuration (database, Kafka, etc.)
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── stockpricegenerator/
        │           ├── StockPriceGeneratorApplicationTests.java     // Basic context load tests
        │           ├── controller/
        │           │   └── StockPriceControllerTest.java              // Tests for REST endpoints
        │           ├── service/
        │           │   └── StockPriceServiceTest.java                 // Unit tests for service layer
        │           └── scheduler/
        │               └── StockPriceSchedulerTest.java               // Tests for scheduled generation
        └── resources/
            └── application-test.yml                                 // Test configuration (H2 in-memory database)
