package com.demo.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data

@ToString
public class ProductRequest implements Request {

	@JsonProperty("name")
	private String name;

	@JsonProperty("price")
	private Double price;

	@JsonProperty("order_id")
	private Long orderId;
}
