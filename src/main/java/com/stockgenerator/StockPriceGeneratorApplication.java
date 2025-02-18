package com.stockgenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Stock Price Generator microservice.
 *
 * <p>This Spring Boot application is responsible for generating mock stock prices
 * and publishing them to a Kafka topic. The application uses Kafka to stream
 * real-time stock price updates to consumers.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Kafka Producer for streaming stock prices.</li>
 *   <li>Spring Boot for easy setup and configuration.</li>
 *   <li>Configurable stock symbols and intervals.</li>
 * </ul>
 *
 * <p>Run this application using:</p>
 * <pre>
 *   ./gradlew bootRun
 * </pre>
 *
 * Or using Docker (if Dockerized):
 * <pre>
 *   docker-compose up --build
 * </pre>
 *
 * @author Dzmitry Ivaniuta
 * @version 1.0
 * @since 2025-02-17
 */
@SpringBootApplication
@Slf4j
public class StockPriceGeneratorApplication {

    /**
     * Main method to launch the Stock Price Generator application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(StockPriceGeneratorApplication.class, args);
        log.info("Stock Price Generator Application is running...");
    }
}
