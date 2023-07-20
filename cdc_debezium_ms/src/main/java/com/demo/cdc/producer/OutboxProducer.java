package com.demo.cdc.producer;

import static com.demo.cdc.utils.Constants.CDC_OUTBOX_1;
import static com.demo.cdc.utils.Constants.CDC_OUTBOX_RETRY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.demo.cdc.model.Outbox;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OutboxProducer {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	/**
	 * Sends the message contained in the Outbox to the Kafka topic. This method is
	 * annotated with {@code @Retryable} to indicate that it should be retried in
	 * case of exceptions. It sets the 'published' flag of the Outbox to true,
	 * serializes it to JSON, and sends it to the Kafka topic using the
	 * KafkaTemplate.
	 *
	 * @param outbox The Outbox containing the message to be sent.
	 * @throws JsonProcessingException If there is an issue with JSON processing.
	 */
	@Retryable(value = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000))
	// nota il retryaBle ha senso se e solo se si tratta di una coda, Queue di topic
	// senno si perde l ordine
	public void sendMessage(Outbox outbox) throws JsonProcessingException {
		outbox.setPublished(true);
		var json = objectMapper.writeValueAsString(outbox);
		kafkaTemplate.send(CDC_OUTBOX_1, json);
	}

	/**
	 * Recovers from an exception during message sending by retrying the message in
	 * a different Kafka topic. This method is annotated with {@code @Recover} to
	 * indicate that it is the recovery method for the retryable operation. It sets
	 * the 'published' flag of the Outbox to true, serializes it to JSON, and sends
	 * it to a retry Kafka topic using the KafkaTemplate.
	 *
	 * @param t      The exception that triggered the recovery.
	 * @param outbox The Outbox containing the message to be retried.
	 * @throws JsonProcessingException If there is an issue with JSON processing.
	 */
	@Recover
	public void recover(Exception t, Outbox outbox) throws JsonProcessingException {
		outbox.setPublished(true);
		var json = objectMapper.writeValueAsString(outbox);
		kafkaTemplate.send(CDC_OUTBOX_RETRY, json);
	}

}
