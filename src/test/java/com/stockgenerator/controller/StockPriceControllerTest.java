package com.stockgenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockgenerator.config.TestConfig;
import com.stockgenerator.controller.StockPriceController;
import com.stockgenerator.model.StockPrice;
import com.stockgenerator.service.StockPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link StockPriceController}.
 * This test uses a custom test configuration (TestConfig) to supply a
 * valid Spring Boot configuration. It also uses @MockitoBean to inject a mock
 * for StockPriceService into the Spring context.
 */
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class StockPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockPriceService stockPriceService;

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        // Registers JavaTimeModule
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void printMappings() {
        handlerMapping.getHandlerMethods().forEach((mapping, handlerMethod) -> {
            System.out.println(mapping + " ==> " + handlerMethod);
        });
    }

    /**
     * Tests the endpoint for publishing a stock price.
     */
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testPublishStockPrice() throws Exception {
        StockPrice stock = new StockPrice(null, "AAPL", 150.75, LocalDateTime.now());

        doNothing().when(stockPriceService).publishStockPrice(stock);

        mockMvc.perform(post("/api/stocks/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Stock price published successfully"));
    }

    /**
     * Tests the endpoint for retrieving a stock price by symbol.
     */
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetStockPriceBySymbol() throws Exception {
        StockPrice stock = new StockPrice(1L, "AAPL", 150.75, LocalDateTime.now());

        when(stockPriceService.getStockPriceBySymbol("AAPL")).thenReturn(Optional.of(stock));

        mockMvc.perform(get("/api/stocks/symbol/AAPL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"));
    }

}
