package com.github.tripflow.infrastructure.adapter.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("trip")
public class TripEntity {

    @Id
    private Long tripId;

}
