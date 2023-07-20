package com.demo.consumerapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Outbox {

	private Number id;

	private Number aggregateId;

	private String aggregateType;

	private String eventType;

	private Integer version;

	private String payload;

	private Boolean published;

	private String createdAt;
}
