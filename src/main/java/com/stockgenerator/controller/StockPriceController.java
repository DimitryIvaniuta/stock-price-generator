package com.stockgenerator.controller;

import com.stockgenerator.model.StockPrice;
import com.stockgenerator.producer.StockPriceProducer;
import com.stockgenerator.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;

import java.util.List;

/**
 * REST Controller for managing stock price operations.
 * <p>
 * This controller provides endpoints for retrieving stock prices
 * and publishing new stock price data to a Kafka topic.
 * </p>
 */
@RestController
@RequestMapping("/api/stocks")
public class StockPriceController {

    /**
     * Service for handling stock price business logic and Kafka publishing.
     */
    private final StockPriceService stockPriceService;

    /**
     * Constructs the {@code StockPriceController} with the required service.
     *
     * @param stockPriceService the service handling stock price operations
     */
    @Autowired
    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    /**
     * Retrieves all stock prices.
     * <p>
     * This endpoint returns a list of all available stock prices.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a list of {@link StockPrice} objects
     */
    @GetMapping
    public ResponseEntity<List<StockPrice>> getAllStockPrices() {
        List<StockPrice> stockPrices = stockPriceService.getAllStockPrices();
        if (stockPrices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stockPrices);
    }

    /**
     * Publishes a new stock price to Kafka.
     * <p>
     * This endpoint accepts a JSON request body representing a stock price
     * and sends it to the configured Kafka topic.
     * </p>
     *
     * @param stockPrice the stock price to publish
     * @return a {@link ResponseEntity} indicating the result of the operation
     */
    @PostMapping("/publish")
    public ResponseEntity<String> publishStockPrice(@RequestBody StockPrice stockPrice) {
        try {
            stockPriceService.publishStockPrice(stockPrice);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Stock price published successfully: " + stockPrice);
        } catch (KafkaException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to publish stock price due to Kafka error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while publishing stock price: " + e.getMessage());
        }
    }

}
