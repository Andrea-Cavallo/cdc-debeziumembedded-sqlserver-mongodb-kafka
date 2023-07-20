package com.demo.cdc.mapper;

import static com.demo.cdc.utils.Constants.AGGREGATE_ID;
import static com.demo.cdc.utils.Constants.AGGREGATE_TYPE;
import static com.demo.cdc.utils.Constants.CREATED_AT;
import static com.demo.cdc.utils.Constants.EVENT_TYPE;
import static com.demo.cdc.utils.Constants.ID;
import static com.demo.cdc.utils.Constants.PAYLOAD;
import static com.demo.cdc.utils.Constants.PUBLISHED;
import static com.demo.cdc.utils.Constants.VERSION;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.demo.cdc.model.Outbox;
import com.demo.cdc.utils.CDCUtils;

@Component
public class CDCMappers {

	public Outbox convertToOutbox(Map<String, Object> message) {
		return Outbox.builder().aggregateId(CDCUtils.convertToNumber(message.get(AGGREGATE_ID)))
				.aggregateType(CDCUtils.toStringValue(message.get(AGGREGATE_TYPE)))
				.eventType(CDCUtils.toStringValue(message.get(EVENT_TYPE)))
				.version(CDCUtils.toIntegerValue(message.get(VERSION)))
				.payload(CDCUtils.toStringValue(message.get(PAYLOAD))).published((Boolean) message.get(PUBLISHED))
				.createdAt(Instant.ofEpochMilli(CDCUtils.toLongValue(message.get(CREATED_AT))))
				.id(CDCUtils.convertToNumber(message.get(ID))).build();
	}

}
