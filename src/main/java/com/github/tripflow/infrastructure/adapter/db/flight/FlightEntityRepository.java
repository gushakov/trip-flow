package com.github.tripflow.infrastructure.adapter.db.flight;

import org.springframework.data.repository.CrudRepository;

public interface FlightEntityRepository extends CrudRepository<FlightEntity, String> {
}
