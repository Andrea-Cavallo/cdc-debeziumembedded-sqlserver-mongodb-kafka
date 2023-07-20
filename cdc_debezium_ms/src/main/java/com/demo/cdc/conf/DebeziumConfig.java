package com.demo.cdc.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebeziumConfig {

	/**
	 * Outbox Database details.
	 */
	@Value("${outbox.datasource.host}")
	private String outboxDBHost;

	@Value("${outbox.datasource.databasename}")
	private String outboxDBName;

	@Value("${outbox.datasource.port}")
	private String outboxDBPort;

	@Value("${outbox.datasource.username}")
	private String outboxDBUserName;

	@Value("${outbox.datasource.password}")
	private String outboxDBPassword;

	private String OUTBOX_TABLE_NAME = "dbo.outbox";

	/**
	 * Outbox database connector.
	 *
	 * @return Configuration.
	 */
	@Bean
	public io.debezium.config.Configuration outboxConnector() {
		return io.debezium.config.Configuration.create()
				.with("connector.class", "io.debezium.connector.sqlserver.SqlServerConnector")
				.with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
				.with("offset.storage.file.filename", "la vostra directory \\offset-dat.dat")
				.with("offset.flush.interval.ms", 60000)
				.with("name", "outbox-connector")
				.with("database.server.name", "server name")
				.with("database.hostname", "il vostro hostname")
				.with("database.history.kafka.bootstrap.servers", "localhost:29092")
				.with("database.history.kafka.topic", "dbo.outbox")
				.with("database.port", outboxDBPort)
				.with("database.user", outboxDBUserName)
				.with("database.password", outboxDBPassword)
				.with("database.dbname", outboxDBName)
				.with("include.schema.changes", "false")
				.with("table.whitelist", OUTBOX_TABLE_NAME).build();
	}
}
