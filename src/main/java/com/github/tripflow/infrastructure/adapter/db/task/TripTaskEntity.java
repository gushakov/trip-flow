package com.github.tripflow.infrastructure.adapter.db.task;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table("trip_task")
public class TripTaskEntity {

    @Id
    @Column("task_id")
    Long taskId;

    @Column("trip_id")
    Long tripId;

    @Column("name")
    String name;

    @Column("action")
    String action;

    @Column("trip_started_by")
    String tripStartedBy;

    @Column("candidate_groups")
    String candidateGroups;

    @Column("version")
    @Version
    Integer version;

}
