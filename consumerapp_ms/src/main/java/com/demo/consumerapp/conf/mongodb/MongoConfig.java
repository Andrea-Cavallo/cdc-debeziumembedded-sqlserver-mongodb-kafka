package com.demo.consumerapp.conf.mongodb;

import java.util.List;
import java.util.StringJoiner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig extends AbstractMongoClientConfiguration {

	private final MongoProperties mongoProperties;

	@Override
	protected String getDatabaseName() {
		return mongoProperties.getDatabase();
	}

	@Bean
	@Override
	public MongoCustomConversions customConversions() {
		return new MongoCustomConversions(List.of(new LocalDateTimeReadConverter(), new LocalDateTimeWriteConverter()));
	}

	@Bean
	@Override
	public MongoClient mongoClient() {
		StringJoiner connectionString = new StringJoiner("&");
		connectionString.add(mongoProperties.getUri()).add("minPoolSize=" + mongoProperties.getMinPoolSize())
				.add("maxPoolSize=" + mongoProperties.getMaxPoolSize());

		return MongoClients.create(connectionString.toString());
	}

}
