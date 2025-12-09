package com.demo.kafkademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "demo-topic";

    public void sendMessage(String message) {

        //CompletableFuture is basicly used for async programming in java 8 and above. Ex. it allows you to write non-blocking code.
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message: " + message + " with offset: " +
                        result.getRecordMetadata().offset());
            } else {
                System.err.println("Unable to send message: " + message + " due to: " + ex.getMessage());
            }
        });
    }
}