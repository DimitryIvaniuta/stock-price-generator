package com.stockgenerator.config;

import com.stockgenerator.model.StockPrice;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up Kafka producer properties and topics.
 * <p>
 * This class configures the Kafka producer factory and template to send stock price updates.
 * It also ensures that the necessary topics are created automatically if they do not exist.
 * </p>
 */
@Configuration
public class KafkaConfig {

    /**
     * The address of the Kafka broker.
     * Default Kafka port 9092 on localhost.
     */
    private static final String KAFKA_BROKER = "localhost:9092";

    /**
     * The topic name for publishing stock price updates.
     */
    private static final String STOCK_TOPIC = "stock-price-topic";

    /**
     * Defines the configuration properties for the Kafka producer.
     *
     * @return a map containing Kafka producer configurations.
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensures all replicas acknowledge
        props.put(ProducerConfig.RETRIES_CONFIG, 3);  // Number of retries for sending
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1); // Adds a small delay for batching
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Prevents duplicates
        return props;
    }

    /**
     * Creates a producer factory using the configured producer properties.
     * <p>
     * The producer factory is responsible for creating Kafka producers.
     * </p>
     *
     * @return the {@link ProducerFactory} for Kafka.
     */
    @Bean
    public ProducerFactory<String, StockPrice> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Creates a Kafka template for sending messages to Kafka.
     * <p>
     * The Kafka template wraps the producer factory and provides convenient methods for sending messages.
     * </p>
     *
     * @return a {@link KafkaTemplate} for sending messages.
     */
    @Bean
    public KafkaTemplate<String, StockPrice> stockPriceKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Ensures that the "stock-prices" topic is created if it does not exist.
     * <p>
     * The topic is configured with 3 partitions and a replication factor of 1.
     * </p>
     *
     * @return a {@link NewTopic} representing the "stock-price-topic" topic.
     */
    @Bean
    public NewTopic stockPricesTopic() {
        return new NewTopic(STOCK_TOPIC, 3, (short) 1);
    }

}