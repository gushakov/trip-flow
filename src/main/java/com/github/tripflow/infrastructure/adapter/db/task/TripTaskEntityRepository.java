package com.github.tripflow.infrastructure.adapter.db.task;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TripTaskEntityRepository extends CrudRepository<TripTaskEntity, Long> {

    List<TripTaskEntity> findAllByTripIdAndCandidateGroupsAndTripStartedBy(Long tripId, String candidateGroups, String tripStartedBy);

    List<TripTaskEntity> findAllByCandidateGroupsAndTripStartedBy(String candidateGroups, String tripStartedBy);

}
