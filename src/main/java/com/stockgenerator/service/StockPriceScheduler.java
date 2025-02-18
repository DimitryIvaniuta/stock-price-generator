package com.stockgenerator.service;


import com.stockgenerator.model.StockPrice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Scheduler responsible for generating and publishing stock price updates at regular intervals.
 */
@Slf4j
@Component
public class StockPriceScheduler {

    /**
     * Service responsible for handling stock price operations.
     */
    private final StockPriceService stockPriceService;

    /**
     * Random number generator for simulating stock price changes.
     */
    private final Random random = new Random();

    /**
     * Array of predefined stock symbols to simulate.
     */
    private static final String[] STOCK_SYMBOLS = {"AAPL", "GOOG", "MSFT", "AMZN", "TSLA"};

    /**
     * Constructor to inject the stock price service dependency.
     *
     * @param stockPriceService The service handling stock price operations.
     */
    public StockPriceScheduler(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    /**
     * Generates and publishes stock prices every 5 seconds.
     * Automatically updates the database and publishes to Kafka.
     */
    @Scheduled(fixedRateString = "${stock.scheduler.fixed-rate:5000}")
    public void generateStockPrices() {
        try {
            for (String symbol : STOCK_SYMBOLS) {
                double price = 100 + random.nextDouble() * 200; // Random price between 100 and 300
                StockPrice stockPrice = new StockPrice(null, symbol, price, LocalDateTime.now());

                // Save and publish stock price
                stockPriceService.updateStockPriceAndPublish(symbol, price);
                log.info("Generated stock price: {}", stockPrice);
            }
        } catch (Exception e) {
            log.error("Error occurred while generating stock prices: {}", e.getMessage(), e);
        }
    }

}
