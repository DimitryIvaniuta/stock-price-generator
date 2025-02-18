package com.stockgenerator.producer;

import com.stockgenerator.model.StockPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer service for sending stock price updates to the "stock-price-topic" topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockPriceProducer {

    private final KafkaTemplate<String, StockPrice> kafkaTemplate;
    private static final String TOPIC = "stock-price-topic";

    /**
     * Sends a stock price update to the Kafka topic.
     *
     * @param stockPrice the stock price information to send.
     */
    public void sendStockPrice(StockPrice stockPrice) {
        kafkaTemplate.send(TOPIC, stockPrice.getSymbol(), stockPrice);
        log.info("Sent stock price: {}", stockPrice);
    }

}