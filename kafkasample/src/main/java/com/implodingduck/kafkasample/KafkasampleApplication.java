package com.implodingduck.kafkasample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
public class KafkasampleApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkasampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkasampleApplication.class, args);
	}

	

}
