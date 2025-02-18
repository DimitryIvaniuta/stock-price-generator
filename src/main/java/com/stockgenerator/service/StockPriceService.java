package com.stockgenerator.service;

import com.stockgenerator.model.StockPrice;
import com.stockgenerator.repository.StockPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class to manage stock price operations.
 * This includes fetching stock prices and publishing stock price updates to a Kafka topic.
 */
@Service
@Slf4j
public class StockPriceService {

    /**
     * The repository that provides access to stock price data.
     */
    private final StockPriceRepository stockPriceRepository;

    /**
     * Kafka Template to send stock price updates to Kafka topic.
     */
    private final KafkaTemplate<String, StockPrice> kafkaTemplate;

    /**
     * The Kafka topic where stock price updates are published.
     */
    private static final String STOCK_PRICE_TOPIC = "stock-price-topic";

    /**
     * Constructor for StockPriceService.
     * Initializes the service with the provided repository and KafkaTemplate.
     *
     * @param stockPriceRepository Repository to manage stock price data.
     * @param kafkaTemplate KafkaTemplate to send messages to Kafka.
     */
    @Autowired
    public StockPriceService(
            StockPriceRepository stockPriceRepository,
            @Qualifier("stockPriceKafkaTemplate")
                    KafkaTemplate<String, StockPrice> kafkaTemplate
    ) {
        this.stockPriceRepository = stockPriceRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Retrieves all stock prices from the database.
     *
     * @return A list of all StockPrice objects.
     */
    public List<StockPrice> getAllStockPrices() {
        return stockPriceRepository.findAll();
    }

    /**
     * Retrieves a stock price by its symbol.
     *
     * @param symbol The stock symbol (e.g., "AAPL", "GOOG").
     * @return An Optional containing the StockPrice if found, or an empty Optional if not found.
     */
    public Optional<StockPrice> getStockPriceBySymbol(String symbol) {
        return stockPriceRepository.findBySymbol(symbol);
    }

    /**
     * Updates the price of an existing stock or creates a new stock price entry if the stock does not exist.
     *
     * @param stockPrice The StockPrice object containing the updated price information.
     * @return The updated or newly created StockPrice object.
     */
    public StockPrice updateStockPrice(StockPrice stockPrice) {
        // Check if the stock already exists in the database
        Optional<StockPrice> existingStockPrice = stockPriceRepository.findBySymbol(stockPrice.getSymbol());

        if (existingStockPrice.isPresent()) {
            // Update the existing stock price
            StockPrice existing = existingStockPrice.get();
            existing.setPrice(stockPrice.getPrice());
            existing.setTimestamp(stockPrice.getTimestamp());
            return stockPriceRepository.save(existing);
        } else {
            // Create a new stock price
            return stockPriceRepository.save(stockPrice);
        }
    }

    /**
     * Publishes a stock price to the Kafka topic.
     *
     * @param stockPrice The stock price to publish.
     */
    public void publishStockPrice(StockPrice stockPrice) {
        try {
            kafkaTemplate.send(STOCK_PRICE_TOPIC, stockPrice.getSymbol(), stockPrice);
            log.info("Published stock price to topic {}: {}", STOCK_PRICE_TOPIC, stockPrice);
        } catch (Exception e) {
            log.error("Failed to publish stock price: {}", e.getMessage(), e);
            throw new RuntimeException("Error publishing stock price", e);
        }
    }

    /**
     * Retrieves the latest stock price by symbol and publishes an update to Kafka.
     * If no stock price is found for the given symbol, it creates a new entry.
     *
     * @param symbol The stock symbol (e.g., "AAPL", "GOOG").
     * @param price The latest stock price to be updated.
     */
    public void updateStockPriceAndPublish(String symbol, double price) {
        Optional<StockPrice> stockPriceOptional = stockPriceRepository.findBySymbol(symbol);

        StockPrice stockPrice;

        if (stockPriceOptional.isPresent()) {
            // Stock exists, update the price
            stockPrice = stockPriceOptional.get();
            stockPrice.setPrice(price);
            stockPrice.setTimestamp(LocalDateTime.now());
        } else {
            // Stock doesn't exist, create a new record
            stockPrice = new StockPrice(null, symbol, price, LocalDateTime.now());
        }

        // Save the updated or newly created stock price
        stockPriceRepository.save(stockPrice);

        // Publish the stock price update to Kafka
        updateStockPrice(stockPrice);
    }

    /**
     * Deletes a stock price by its symbol.
     *
     * @param symbol The symbol of the stock to be deleted.
     * @throws IllegalArgumentException if the stock symbol does not exist.
     */
    public void deleteStockPrice(String symbol) {
        Optional<StockPrice> stockPrice = stockPriceRepository.findBySymbol(symbol);
        if (stockPrice.isPresent()) {
            stockPriceRepository.delete(stockPrice.get());
        } else {
            throw new IllegalArgumentException("Stock with symbol " + symbol + " does not exist.");
        }
    }

    /**
     * Clears all stock price records from the database.
     * This method removes all entries in the stock price repository.
     */
    public void clearAllStockPrices() {
        stockPriceRepository.deleteAll();
    }
}
