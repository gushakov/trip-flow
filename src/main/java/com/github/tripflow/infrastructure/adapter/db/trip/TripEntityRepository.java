package com.github.tripflow.infrastructure.adapter.db.trip;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface TripEntityRepository extends CrudRepository<TripEntity, Long> {

    List<TripEntity> findAllByStartedByAndStatusNotIn(String startedBy, Collection<String> excludeStatus);

}
