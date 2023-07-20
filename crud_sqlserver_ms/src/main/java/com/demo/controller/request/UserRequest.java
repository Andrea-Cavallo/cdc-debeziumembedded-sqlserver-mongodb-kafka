package com.demo.controller.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequest implements Request {

	private String name;
	private String surname;

}
