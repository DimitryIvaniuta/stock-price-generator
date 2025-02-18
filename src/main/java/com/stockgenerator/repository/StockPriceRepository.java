package com.stockgenerator.repository;

import com.stockgenerator.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link StockPrice} entities.
 *
 * This interface extends {@link JpaRepository}, providing built-in methods for
 * database interactions such as saving, deleting, and finding records.
 */
@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

    /**
     * Finds a stock price record by its symbol.
     *
     * @param symbol The stock symbol (e.g., "AAPL", "GOOG").
     * @return An {@link Optional} containing the found {@link StockPrice}
     *         or an empty {@link Optional} if no match is found.
     */
    Optional<StockPrice> findBySymbol(String symbol);

}