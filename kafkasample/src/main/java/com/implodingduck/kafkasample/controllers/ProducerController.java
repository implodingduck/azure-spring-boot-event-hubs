package com.implodingduck.kafkasample.controllers;

import com.implodingduck.kafkasample.models.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.kafka.support.SendResult;

@RestController
@RequestMapping("/api/producer")
public class ProducerController {

    @Value(value = "${kafka.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public MyEvent produceMyEvent(@RequestBody MyEvent event) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, event.getMessage());
	
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + event.getMessage() + 
                "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" 
                + event.getMessage() + "] due to : " + ex.getMessage());
            }
        });
        return event;
    }
}
