package com.demo.mapper;

import org.springframework.stereotype.Component;

import com.demo.controller.request.UserRequest;
import com.demo.model.User;

@Component
public class UserMapper {

	public User toUser(UserRequest userRequest) {
		User user = new User();
		user.setName(userRequest.getName());
		user.setSurname(userRequest.getSurname());
		return user;
	}
}
