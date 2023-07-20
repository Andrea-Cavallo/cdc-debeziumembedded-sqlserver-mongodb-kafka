package com.demo.cdc.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import com.demo.cdc.dispatcher.Dispatcher;
import com.demo.cdc.model.enums.Operation;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CDCListener {

	/**
	 * Single thread pool which will run the Debezium engine asynchronously.
	 */
	private final Executor executor = Executors.newSingleThreadExecutor();

	/**
	 * The Debezium engine which needs to be loaded with the configurations, Started
	 * and Stopped - for the CDC to work.
	 */
	private final EmbeddedEngine engine;

	/**
	 * Handle to the Service layer, which interacts with ElasticSearch.
	 */
	private final Dispatcher dispatcher;

	/**
	 * Constructor which loads the configurations and sets a callback method
	 * 'handleEvent', which is invoked when a DataBase transactional operation is
	 * performed.
	 *
	 * @param studentConnector
	 * @param studentService
	 */
	private CDCListener(Configuration studentConnector, Dispatcher dispatcher) {
		this.engine = EmbeddedEngine.create().using(studentConnector).notifying(this::handleEvent).build();

		this.dispatcher = dispatcher;
	}

	/**
	 * The method is called after the Debezium engine is initialized and started
	 * asynchronously using the Executor.
	 */
	@PostConstruct
	private void start() {
		this.executor.execute(engine);
	}

	/**
	 * This method is called when the container is being destroyed. This stops the
	 * debezium, merging the Executor.
	 */
	@PreDestroy
	private void stop() {
		if (this.engine != null) {
			this.engine.stop();
		}
	}

	/**
	 * This method is invoked when a transactional action is performed on any of the
	 * tables that were configured.
	 *
	 * @param sourceRecord
	 */
	private void handleEvent(SourceRecord sourceRecord) {
		log.info("Tutto il sourceRecord letto : {}", sourceRecord.toString());
		log.info("Il Topic : {}", sourceRecord.topic());
		Struct sourceRecordValue = (Struct) sourceRecord.value();

		Optional.ofNullable(sourceRecordValue).ifPresent(value -> {
			Operation operation = Operation.forCode(value.getString("op"));

			Struct after = (Struct) value.get("after");
			Map<String, Object> mapOfRecord = structToMap(after);
			log.info("Cosa c'è nell'after :  {}", after);
			log.info("L'Operazione è : {}", operation);
			try {
				dispatcher.dispatch(operation, mapOfRecord);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

	private Map<String, Object> structToMap(Struct struct) {
		Map<String, Object> map = new HashMap<>();
		for (Field field : struct.schema().fields()) {
			Object fieldValue = struct.get(field);
			map.put(field.name(), fieldValue);
		}
		return map;
	}

}
