package com.demo.consumerapp.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.consumerapp.model.Outbox;
import com.demo.consumerapp.model.User;
import com.demo.consumerapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public Optional<User> saveUserFromOutbox(Outbox outbox) {
		try {
			String payload = outbox.getPayload();
			User user = fromPayloadToUser(payload);
			userRepository.save(user);
			logger.info("User saved successfully with id: {}", user.getId());
			return Optional.of(user);
		} catch (IOException e) {
			logger.error("Error while converting payload to User object: {}", outbox.getPayload(), e);
			return Optional.empty();
		}
	}

	public static User fromPayloadToUser(String payload) throws JsonProcessingException {

		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(payload,
				User.class);

	}

}
