package com.demo.consumerapp.consumer;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.demo.consumerapp.model.Outbox;
import com.demo.consumerapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OutboxConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OutboxConsumer.class);
	private final UserService userService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RetryTemplate retryTemplate;

	public OutboxConsumer(UserService userService, RetryTemplate retryTemplate) {
		this.userService = userService;
		this.retryTemplate = retryTemplate;
	}

	@KafkaListener(topics = "cdc-outbox-topic", containerFactory = "outboxKafkaListenerContainerFactory", groupId = "outbox-service")
	public void consume(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
		logger.info("Received message from topic 'cdc-outbox-topic': {}", consumerRecord.value());

		retryTemplate.execute(retryContext -> {
			try {
				Outbox outbox = objectMapper.readValue(consumerRecord.value(), Outbox.class);
				userService.saveUserFromOutbox(outbox);
				acknowledgment.acknowledge();
			} catch (IOException e) {
				logger.error("Error while converting message to Outbox object: {}", consumerRecord.value(), e);
				// questa è un eccezione per far scattare il RetryTemplate
				throw new RuntimeException(e);
			}
			return null;
		});
	}
}
