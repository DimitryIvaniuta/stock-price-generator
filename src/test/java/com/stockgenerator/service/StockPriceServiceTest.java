package com.stockgenerator.service;

import com.stockgenerator.model.StockPrice;
import com.stockgenerator.repository.StockPriceRepository;
import com.stockgenerator.service.StockPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Verify that the service correctly saves or updates a stock price,
 * and publishes updates to the Kafka topic.
 */
@ExtendWith(MockitoExtension.class)
class StockPriceServiceTest {

    @Mock
    private StockPriceRepository stockPriceRepository;

    @Mock
    private KafkaTemplate<String, StockPrice> kafkaTemplate;

    @InjectMocks
    private StockPriceService stockPriceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests updating (or creating) a new stock price when no existing record is found.
     */
    @Test
    void testUpdateStockPriceAndPublish_NewStock() {
        String symbol = "AAPL";
        double price = 150.0;

        // Simulate that no stock exists initially.
        when(stockPriceRepository.findBySymbol(symbol)).thenReturn(Optional.empty());

        // When saving, return a stock with an assigned ID.
        StockPrice newStock = new StockPrice(null, symbol, price, LocalDateTime.now());
        StockPrice savedStock = new StockPrice(1L, symbol, price, LocalDateTime.now());
        when(stockPriceRepository.save(any(StockPrice.class))).thenReturn(savedStock);

        // Act: Update or create and publish stock price.
        StockPrice result = stockPriceService.updateStockPriceAndPublish(symbol, price);

        // Assert: The result is not null and contains the expected symbol and price.
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(price, result.getPrice());

        // Verify interactions:
        verify(stockPriceRepository, times(1)).findBySymbol(symbol);
        verify(stockPriceRepository, times(1)).save(any(StockPrice.class));
        verify(kafkaTemplate, times(1))
                .send(eq("stock-price-topic"), eq(symbol), any(StockPrice.class));
    }

    /**
     * Tests updating an existing stock price.
     */
    @Test
    void testUpdateStockPriceAndPublish_ExistingStock() {
        String symbol = "AAPL";
        double newPrice = 160.0;
        LocalDateTime oldTimestamp = LocalDateTime.now().minusHours(1);
        StockPrice existingStock = new StockPrice(1L, symbol, 150.0, oldTimestamp);

        // Simulate existing record.
        when(stockPriceRepository.findBySymbol(symbol)).thenReturn(Optional.of(existingStock));

        // When saving, return the updated stock.
        StockPrice updatedStock = new StockPrice(1L, symbol, newPrice, LocalDateTime.now());
        when(stockPriceRepository.save(any(StockPrice.class))).thenReturn(updatedStock);

        // Act: Update the stock price.
        StockPrice result = stockPriceService.updateStockPriceAndPublish(symbol, newPrice);

        // Assert: The updated stock should have the new price.
        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(newPrice, result.getPrice());

        // Verify interactions:
        verify(stockPriceRepository, times(1)).findBySymbol(symbol);
        verify(stockPriceRepository, times(1)).save(any(StockPrice.class));
        verify(kafkaTemplate, times(1))
                .send(eq("stock-price-topic"), eq(symbol), any(StockPrice.class));
    }

    /**
     * Tests that the publishStockPrice method throws a RuntimeException if Kafka publishing fails.
     */
    @Test
    void testPublishStockPrice_Exception() {
        StockPrice stock = new StockPrice(1L, "GOOG", 2750.0, LocalDateTime.now());
        // Simulate an exception when publishing to Kafka.
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate)
                .send(anyString(), anyString(), any(StockPrice.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            stockPriceService.publishStockPrice(stock);
        });
        assertTrue(exception.getMessage().contains("Error publishing stock price"));

        verify(kafkaTemplate, times(1))
                .send(eq("stock-price-topic"), eq("GOOG"), any(StockPrice.class));
    }

}