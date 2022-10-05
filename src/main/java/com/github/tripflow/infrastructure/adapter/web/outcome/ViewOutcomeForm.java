package com.github.tripflow.infrastructure.adapter.web.outcome;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ViewOutcomeForm {

    long taskId;
    boolean success;
    String tripId;
    String city;

}
