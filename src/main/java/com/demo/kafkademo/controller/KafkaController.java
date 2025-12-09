package com.demo.kafkademo.controller;

import com.demo.kafkademo.service.KafkaProducerService;
import com.demo.kafkademo.listener.KafkaConsumerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private KafkaConsumerListener consumerListener;

    // ✅ New endpoint to send messages to Kafka
    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);
        return "Message sent!";
    }

    // ✅ New endpoint to check if the service is running
    @GetMapping("/ping")
    public String ping() {
        return "Kafka service is running!";
    }

    // ✅ New endpoint to retrieve consumed messages
    @GetMapping("/messages")
    public List<String> getMessages() {
        return consumerListener.getMessages();
    }
}
