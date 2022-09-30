package com.github.tripflow.infrastructure.adapter.db.trip;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("trip")
public class TripEntity {

    @Id
    private Long tripId;

    @Column("status")
    private String status;

    @Column("started_by")
    private String startedBy;

    @Column("flight_number")
    private String flightNumber;

    @Column("hotel_id")
    private String hotelId;

}
