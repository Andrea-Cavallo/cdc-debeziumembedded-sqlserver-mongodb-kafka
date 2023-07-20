package com.demo.cdc.dispatcher;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.demo.cdc.mapper.CDCMappers;
import com.demo.cdc.model.Outbox;
import com.demo.cdc.model.enums.Operation;
import com.demo.cdc.producer.OutboxProducer;
import com.demo.cdc.transcode.Transcoder;
import com.demo.cdc.validators.Validators;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Dispatcher {

	public Dispatcher(Transcoder transcoder, CDCMappers cdcMapper, Validators validator,
			OutboxProducer outboxProducer) {
		super();
		this.transcoder = transcoder;
		this.outboxProducer = outboxProducer;
		this.cdcMapper = cdcMapper;
		this.validator = validator;

	}

	private final Transcoder transcoder;
	private final OutboxProducer outboxProducer;
	private final CDCMappers cdcMapper;
	private final Validators validator;

	/**
	 * Dispatches the operation and processes the source record value.
	 *
	 * @param operation         The operation to be dispatched.
	 * @param sourceRecordValue The source record value to be processed.
	 * @throws JsonProcessingException If there is an issue with JSON processing.
	 */
	public void dispatch(Operation operation, Map<String, Object> entityMap) throws JsonProcessingException {

		switch (operation) {
		case CREATE:
		case UPDATE:
			Outbox entityCreated = processRecord(entityMap);
			log.info("ENTITA DA VALIDARE {}", entityCreated);
			validator.validate(entityCreated);
			Outbox outboxCreated = transcoder.transcode(entityCreated);
			outboxProducer.sendMessage(outboxCreated);
			break;
		case DELETE:
			Outbox entityDeleted = processRecord(entityMap);
			validator.validate(entityDeleted);
			Outbox outboxToDeleted = transcoder.transcode(entityDeleted);
			outboxProducer.sendMessage(outboxToDeleted);
			break;
		case READ:
			break;
		default:
			throw new IllegalStateException("Operation not supported must be UPDATE,CREATE,DELETE or UPDATE");
		}
	}

	private Outbox processRecord(Map<String, Object> sourceRecordValue) {
		log.info("Cosa c'è nel sourceRecord {}", sourceRecordValue);
		try {
			return cdcMapper.convertToOutbox(sourceRecordValue);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("The provided record does not match the structure of Outbox Entity.");
		}
	}

}
