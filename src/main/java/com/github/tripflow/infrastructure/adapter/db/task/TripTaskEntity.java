package com.github.tripflow.infrastructure.adapter.db.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("activated_jobs")
public class TripTaskEntity {

    @Id
    @Column("job_key")
    private Long jobKey;

    @Column("tripId")
    private Long tripId;

    @Column("name")
    private String name;

    @Column("action")
    private String action;

    @Column("trip_started_by")
    private String tripStartedBy;
}
