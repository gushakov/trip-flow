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

    @Column("started_by")
    private String startedBy;

    @Column("flight_number")
    private String flightNumber;

    @Column("hotel_id")
    private String hotelId;

    @Column("flight_booked")
    private boolean flightBooked;

    @Column("hotel_reserved")
    private boolean hotelReserved;

    @Column("trip_cancelled")
    private boolean tripCancelled;

    @Column("trip_confirmed")
    private boolean tripConfirmed;

}
