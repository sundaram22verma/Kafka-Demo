package com.demo.kafkademo.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class KafkaConsumerListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);

    // Thread-safe list to store consumed messages
    private final List<String> messages = new CopyOnWriteArrayList<>();

    //KafkaListener annotation marks a method to be the target of a Kafka message listener on the specified topics.
    @KafkaListener(topics = "demo-topic", groupId = "my-group")
    public void listen(String message) {
        logger.info("Received message: {}", message);
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
