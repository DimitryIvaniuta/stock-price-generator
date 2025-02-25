package com.stockgenerator.scheduler;

import com.stockgenerator.StockPriceGeneratorApplication;
import com.stockgenerator.service.StockPriceScheduler;
import com.stockgenerator.service.StockPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StockPriceScheduler}.
 */
@SpringBootTest(classes = StockPriceGeneratorApplication.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class StockPriceSchedulerTest {

    @MockitoBean
    private StockPriceService stockPriceService;

    @InjectMocks
    private StockPriceScheduler stockPriceScheduler;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests if stock prices are generated and published correctly.
     */
    @Test
    void testGenerateStockPrices() {
        // Act
        stockPriceScheduler.generateStockPrices();

        // Assert: Verify service was called for each stock symbol
        verify(stockPriceService, times(5))
                .updateStockPriceAndPublish(anyString(), anyDouble());

        // Capture arguments to check if the symbols are from the predefined list
        ArgumentCaptor<String> symbolCaptor = ArgumentCaptor.forClass(String.class);
        verify(stockPriceService, times(5))
                .updateStockPriceAndPublish(symbolCaptor.capture(), anyDouble());

        List<String> symbols = symbolCaptor.getAllValues();
        assertEquals(5, symbols.size());
        List<String> expectedSymbols = List.of("AAPL", "GOOG", "MSFT", "AMZN", "TSLA");
        assertEquals(expectedSymbols.size(), symbols.stream().filter(expectedSymbols::contains).count());
    }

    /**
     * Tests the behavior when the service throws an exception.
     */
    @Test
    void testGenerateStockPricesExceptionHandling() {
        // Arrange: Simulate exception on every call
        doAnswer(invocation -> {
            throw new RuntimeException("Kafka publish failed");
        }).when(stockPriceService).updateStockPriceAndPublish(anyString(), anyDouble());

        // Act
        stockPriceScheduler.generateStockPrices();

        // Assert: Ensure service was called 5 times despite exceptions
        verify(stockPriceService, times(5))
                .updateStockPriceAndPublish(anyString(), anyDouble());
    }

}