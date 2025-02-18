package com.stockgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a stock price record.
 */
@Entity
@Table(name = "stock_prices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice {

    /** Unique identifier for the stock price record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Symbol of the stock (e.g., AAPL, MSFT). */
    @Column(nullable = false)
    private String symbol;

    /** Price of the stock. */
    @Column(nullable = false)
    private Double price;

    /** Timestamp when the stock price was recorded. */
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
