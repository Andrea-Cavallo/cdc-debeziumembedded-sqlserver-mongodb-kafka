package com.demo.cdc.validators;

import static com.demo.cdc.utils.Constants.AGGREGATION_ID_MUST_BE_PRESENT;
import static com.demo.cdc.utils.Constants.NAME_MUST_BE_PRESENT;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.demo.cdc.exception.ValidationException;
import com.demo.cdc.model.Entity;
import com.demo.cdc.model.Outbox;
import com.demo.cdc.model.User;

@Component
public class Validators {

	public Validators() {
		super();

	}

	/**
	 * Validates the provided entity. This method is for illustrative purposes only
	 * to demonstrate that an additional validation layer could be added. It checks
	 * specific properties of the entity and throws a ValidationException if the
	 * validation fails.
	 *
	 * @param entity The entity to be validated.
	 * @throws ValidationException If the validation fails.
	 */
	public void validate(Entity entity) {

		if (entity instanceof Outbox) {
			Outbox outbox = (Outbox) entity;
			if (Objects.isNull(outbox.getAggregateId())) {
				throw new ValidationException(AGGREGATION_ID_MUST_BE_PRESENT);
			}
		}

		if (entity instanceof User) {
			User user = (User) entity;
			if (Objects.isNull(user.getName())) {
				throw new ValidationException(NAME_MUST_BE_PRESENT);
			}
		}

	}
}
