package com.implodingduck.eventhubsample.controllers;

import com.implodingduck.eventhubsample.models.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/producer")
public class ProducerController {

    @Autowired
    private Sinks.Many<Message<String>> many;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public MyEvent produceMyEvent(@RequestBody MyEvent event) {
        many.emitNext(new GenericMessage<>(event.getMessage()), Sinks.EmitFailureHandler.FAIL_FAST);
        return event;
    }
}
