package com.github.tripflow.infrastructure.adapter.db.flight;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("flight")
public class FlightEntity {

    @Id
    @Column("flight_number")
    private String flightNumber;

    @Column("airline")
    private String airline;

    @Column("origin_city")
    private String originCity;

    @Column("origin_iata_code")
    private String originIataCode;

    @Column("destination_city")
    private String destinationCity;

    @Column("destination_iata_code")
    private String destinationIataCode;

    @Column("price")
    private int price;


}
