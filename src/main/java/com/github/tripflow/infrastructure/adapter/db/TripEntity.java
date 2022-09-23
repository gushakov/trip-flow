package com.github.tripflow.infrastructure.adapter.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nullable;

@Data
@Table("trip")
public class TripEntity {

    @Id
    private Long tripId;

    @Column("started_by")
    private String startedBy;

    @Column("flight_number")
    private String flightNumber;

}
