package com.github.tripflow.infrastructure.adapter.db.trip;

import org.springframework.data.repository.CrudRepository;

public interface TripEntityRepository extends CrudRepository<TripEntity, Long> {
}
