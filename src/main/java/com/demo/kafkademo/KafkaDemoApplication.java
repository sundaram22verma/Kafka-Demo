package com.demo.kafkademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class KafkaDemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(KafkaDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args);
        logger.info("Kafka Demo Application Started on port 8080");
    }
}