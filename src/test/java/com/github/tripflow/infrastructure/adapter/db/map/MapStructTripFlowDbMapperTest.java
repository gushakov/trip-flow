package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntity;
import com.github.tripflow.infrastructure.map.CommonMapStructConvertersImpl;
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

        // flight booked and hotel reserved flags should start with "false"
        assertThat(trip.isFlightBooked()).isFalse();
        assertThat(trip.isHotelReserved()).isFalse();

        TripEntity tripEntity = dbMapper.map(trip);

        assertThat(tripEntity)
                .extracting(TripEntity::getTripId,
                        TripEntity::getStartedBy,
                        TripEntity::isFlightBooked,
                        TripEntity::isHotelReserved)
                .containsExactly(1L, "customer1", false, false);

    }
}
