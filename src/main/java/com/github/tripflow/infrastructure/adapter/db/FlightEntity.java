package com.github.tripflow.infrastructure.adapter.db;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("flight")
public class FlightEntity {

    @Column("flight_number")
    String flightNumber;

    @Column("airline")
    String airline;

    @Column("origin_city")
    String originCity;

    @Column("origin_iata_code")
    String originIataCode;

    @Column("destination_city")
    String destinationCity;

    @Column("destination_iata_code")
    String destinationIataCode;

    @Column("price")
    int price;


}
