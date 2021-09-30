package com.implodingduck.eventhubsample;

import com.azure.spring.integration.core.EventHubHeaders;
import com.azure.spring.integration.core.api.reactor.Checkpointer;
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

import static com.azure.spring.integration.core.AzureHeaders.CHECKPOINTER;
@SpringBootApplication
public class EventhubsampleApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventhubsampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EventhubsampleApplication.class, args);
	}

	@Bean
    public Sinks.Many<Message<String>> many() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

	@Bean
    public Supplier<Flux<Message<String>>> supply(Sinks.Many<Message<String>> many) {
        return () -> many.asFlux()
                         .doOnNext(m -> LOGGER.info("Manually sending message {}", m))
                         .doOnError(t -> LOGGER.error("Error encountered", t));
    }

    @Bean
    public Consumer<Message<String>> consume() {
        return message -> {
            //Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            LOGGER.info("New message received: '{}', partition key: {}, sequence number: {}, offset: {}, enqueued time: {}",
                message.getPayload(),
                message.getHeaders().get(EventHubHeaders.PARTITION_KEY),
                message.getHeaders().get(EventHubHeaders.SEQUENCE_NUMBER),
                message.getHeaders().get(EventHubHeaders.OFFSET),
                message.getHeaders().get(EventHubHeaders.ENQUEUED_TIME)
                );
            // checkpointer.success()
            //     .doOnSuccess(success -> LOGGER.info("Message '{}' successfully checkpointed", message.getPayload()))
            //     .doOnError(error -> LOGGER.error("Exception found", error))
            //     .subscribe();
        };
    }


}
