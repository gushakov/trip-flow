package com.github.tripflow.infrastructure.adapter.db.trip;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
    Reference:
    ---------

    1.  Boolean criteria and naming in Spring Data: https://stackoverflow.com/questions/17600790/query-by-boolean-properties-in-spring-data-jpa-without-using-method-parameters
 */

public interface TripEntityRepository extends CrudRepository<TripEntity, Long> {

    List<TripEntity> findAllByStartedByAndTripRefusedIsFalseAndTripConfirmedIsFalse(String startedBy);

}
