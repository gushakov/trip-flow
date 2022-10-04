package com.github.tripflow.infrastructure.adapter.db.task;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TripTaskEntityRepository extends CrudRepository<TripTaskEntity, Long> {

    List<TripTaskEntity> findAllByTripId(Long tripId);

    List<TripTaskEntity> findAllByTripStartedBy(String tripStartedBy);

}
