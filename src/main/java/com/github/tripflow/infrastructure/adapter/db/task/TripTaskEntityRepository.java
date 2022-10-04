package com.github.tripflow.infrastructure.adapter.db.task;

import org.springframework.data.repository.CrudRepository;

public interface TripTaskEntityRepository extends CrudRepository<TripTaskEntity, Long> {
}
