package com.demo.service;

import static com.demo.utils.Constants.EMPTY_STRING;
import static com.demo.utils.Constants.FAILED_TO_SAVE_OUTBOX_ENTITY_INVALID_DATA;
import static com.demo.utils.Constants.FAILED_TO_SERIALIZE_USER_TO_JSON;
import static com.demo.utils.Constants.NOT_FOUND;
import static com.demo.utils.Constants.OUTBOX_WITH_ID;
import static com.demo.utils.Constants.USER;
import static com.demo.utils.Constants.USER_CREATED;
import static com.demo.utils.Constants.USER_CREATED_WITH_ID_AND_OUTBOX_ENTITY_CREATED;
import static com.demo.utils.Constants.USER_DELETED;
import static com.demo.utils.Constants.USER_UPDATED;
import static com.demo.utils.Constants.USER_WITH_ID;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.controller.request.UserRequest;
import com.demo.exception.OutboxNotFoundException;
import com.demo.exception.UserNotFoundException;
import com.demo.exception.ValidationException;
import com.demo.mapper.UserMapper;
import com.demo.model.Outbox;
import com.demo.model.User;
import com.demo.repository.OutboxRepository;
import com.demo.repository.UserRepository;
import com.demo.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService  {

	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;

	private final OutboxRepository outboxRepository;

	private final UserMapper userMapper;

	public UserService(@Autowired UserRepository userRepository, @Autowired OutboxRepository outboxRepository,
			@Autowired UserMapper userMapper, ObjectMapper objectMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.outboxRepository = outboxRepository;
		this.objectMapper = objectMapper;

	}

	/**
	 * 
	 * Creates a new user based on the provided user request.
	 * 
	 * @param userRequest The user request containing the user data.
	 * 
	 * @return The newly created user.
	 * 
	 * @throws ValidationException If an error occurs while saving the outbox
	 *                             entity.
	 */
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public User create(UserRequest userRequest) {
		

		User user = userMapper.toUser(userRequest);
		User savedUser = userRepository.save(user);
		Long serializedId = savedUser.getId();
		String serializedUser = serializeUser(savedUser);


		Outbox outbox = Outbox.builder()
				.id(Utils.convertUUIDToLong(UUID.randomUUID()))
				.aggregateId(serializedId)
				.aggregateType(USER)
				.eventType(USER_CREATED)
				.version(1)
				.payload(serializedUser)
				.published(false)
				.createdAt(Instant.now()).build();
	
		try {
			outboxRepository.save(outbox);
			log.info(USER_CREATED_WITH_ID_AND_OUTBOX_ENTITY_CREATED, savedUser.getId(), outbox.toString());
	

		} catch (DataAccessException ex) {
			throw new ValidationException(FAILED_TO_SAVE_OUTBOX_ENTITY_INVALID_DATA);

		}
		return user;

	}

	/**
	 * 
	 * Updates an existing user with the provided user request.
	 * 
	 * @param id          The ID of the user to update.
	 * 
	 * @param userRequest The user request containing the updated user data.
	 * 
	 * @return The updated user.
	 * 
	 * @throws UserNotFoundException   If the user with the specified ID is not
	 *                                 found.
	 * 
	 * @throws OutboxNotFoundException If the outbox entity with the specified ID is
	 *                                 not found.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public User update(Long id, UserRequest userRequest) {

		User updatedUser = userRepository.findById(id).map(existingUser -> {
			User updated = userMapper.toUser(userRequest);
			updated.setId(existingUser.getId());
			return userRepository.save(updated);
		}).orElseThrow(() -> new UserNotFoundException(USER_WITH_ID + id + NOT_FOUND));
		String serializedUser = serializeUser(updatedUser);
		Outbox existingOutbox = outboxRepository.findByAggregateId(id)
				.orElseThrow(() -> new OutboxNotFoundException(OUTBOX_WITH_ID + id + NOT_FOUND));
		existingOutbox.setPayload(serializedUser);
		existingOutbox.setEventType(USER_UPDATED);
		outboxRepository.save(existingOutbox);

		return updatedUser;
	}

	/**
	 * 
	 * Deletes a user with the specified ID.
	 * 
	 * @param id The ID of the user to delete.
	 * 
	 * @throws UserNotFoundException   If the user with the specified ID is not
	 *                                 found.
	 * 
	 * @throws OutboxNotFoundException If the outbox entity with the specified ID is
	 *                                 not found.
	 */

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void delete(Long id) {

		User user = find(id);
		Outbox outbox = outboxRepository.findByAggregateId(id)
				.orElseThrow(() -> new OutboxNotFoundException(OUTBOX_WITH_ID + id + NOT_FOUND));
		outbox.setEventType(USER_DELETED);
		outboxRepository.save(outbox);
		userRepository.delete(user);

	}

	/**
	 * 
	 * Finds a user by the specified ID.
	 * 
	 * @param id The ID of the user to find.
	 * @return The user with the specified ID.
	 * @throws UserNotFoundException If the user with the specified ID is not found.
	 */
	public User find(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_WITH_ID + id + NOT_FOUND));
	}

	private String serializeUser(User savedUser) {
		String serializedUser = EMPTY_STRING;
		try {
			serializedUser = objectMapper.writeValueAsString(savedUser);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(FAILED_TO_SERIALIZE_USER_TO_JSON, e);
		}
		return serializedUser;
	}

}
