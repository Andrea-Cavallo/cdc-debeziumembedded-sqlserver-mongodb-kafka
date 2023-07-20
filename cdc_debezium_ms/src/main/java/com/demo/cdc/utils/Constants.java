package com.demo.cdc.utils;

public class Constants {

	private Constants() {
	}

	public static final String PRIMA_DEL_DISPATCH_DATA_IN_INGRESSO_CON_OPERAZIONE = "Prima del Dispatch data in ingresso : {} con Operazione: {}";
	public static final String MISSING_AGGREGATE_ID_IN_THE_OUTBOX = "Missing 'aggregate_id' in the outbox.";
	public static final String AGGREGATE_ID = "aggregate_id";
	public static final String CDC_OUTBOX_RETRY = "cdc-outbox-retry-topic";
	public static final String AGGREGATE_TYPE = "aggregate_type";
	public static final String EVENT_TYPE = "event_type";
	public static final String VERSION = "version";
	public static final String PAYLOAD = "payload";
	public static final String PUBLISHED = "published";
	public static final String ID = "id";
	public static final String CREATED_AT = "created_at";
	public static final String CDC_OUTBOX_1 = "cdc-outbox-topic";
	public static final String AGGREGATION_ID_MUST_BE_PRESENT = "Aggregation Id must be present";
	public static final String NAME_MUST_BE_PRESENT = "Name must be present";
	public static final String THE_TRANSCODED_OUTBOX_ENTITY_IS = "The transcoded outbox entity is {}";
	public static final String EMPTY = " ";
	public static final String ALL = "all";
	public static final String _180000 = "180000";
	public static final String REGISTRY_URL = "http://localhost:8080/apis/registry/v2";
	public static final String SERVERS = "localhost:29092";
}
