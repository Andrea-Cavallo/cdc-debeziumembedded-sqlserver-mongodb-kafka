package com.demo.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.controller.request.UserRequest;
import com.demo.exception.UserNotFoundException;
import com.demo.model.Outbox;
import com.demo.model.User;
import com.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/users")
public class UserController {

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	private UserService userService;

	@Operation(summary = "Crea un nuovo utente")
	@ApiResponse(responseCode = "201", description = "Utente creato con successo", content = @Content(schema = @Schema(implementation = User.class)))
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
		User outbox = userService.create(userRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(outbox.getId())
				.toUri();
		return ResponseEntity.created(location).body(outbox);
	}

	@Operation(summary = "Aggiorna un utente esistente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Utente aggiornato con successo", content = @Content(schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "404", description = "Utente non trovato") })
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
		User updatedUser = userService.update(id, userRequest);
		if (updatedUser != null) {
			return ResponseEntity.ok(updatedUser);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Elimina un utente")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Utente eliminato con successo"),
			@ApiResponse(responseCode = "404", description = "Utente non trovato") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
		try {
			userService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (UserNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}

	}

	@Operation(summary = "Ottieni un utente per ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Utente trovato", content = @Content(schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "404", description = "Utente non trovato") })
	@GetMapping("/{id}")
	public ResponseEntity<User> findUser(@PathVariable("id") Long id) {
		User user = userService.find(id);
		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
