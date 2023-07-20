package com.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Outbox;
@Repository

public interface OutboxRepository extends CrudRepository<Outbox, Long> {

	Optional<Outbox> findByAggregateId(Long aggregateId);
}
