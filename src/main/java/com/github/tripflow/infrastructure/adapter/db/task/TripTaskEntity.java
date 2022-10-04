package com.github.tripflow.infrastructure.adapter.db.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("trip_task")
public class TripTaskEntity {

    @Id
    @Column("task_id")
    private Long taskId;

    @Column("trip_id")
    private Long tripId;

    @Column("name")
    private String name;

    @Column("action")
    private String action;

    @Column("trip_started_by")
    private String tripStartedBy;
}
