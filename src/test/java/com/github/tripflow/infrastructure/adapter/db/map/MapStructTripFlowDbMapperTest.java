package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripStatus;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntity;
import com.github.tripflow.infrastructure.map.CommonMapStructConvertersImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {MapStructTripFlowDbMapperImpl.class, CommonMapStructConvertersImpl.class})
public class MapStructTripFlowDbMapperTest {

    @Autowired
    private MapStructTripFlowDbMapper dbMapper;

    @Test
    void map_trip_to_trip_entity() {
        Trip trip = Trip.builder()
                .tripId(TripId.of(1L))
                .startedBy("customer1")
                .build();

        // status should be undefined
        assertThat(trip.getStatus()).isEqualTo(TripStatus.undefined);

        TripEntity tripEntity = dbMapper.map(trip);

        assertThat(tripEntity)
                .extracting(TripEntity::getTripId,
                        TripEntity::getStartedBy,
                        TripEntity::getStatus)
                .containsExactly(1L, "customer1", "undefined");

    }
}
