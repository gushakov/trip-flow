package com.github.tripflow.core.model;

import java.util.Optional;

public class Validator {

    /**
     * Throws {@code TripFlowValidationError} if {@code something} argument is {@code null}.
     *
     * @param something any object
     * @param <T>       any type
     * @return argument object if not {@code null}
     * @throws TripFlowValidationError if argument is {@code null}
     */
    public static <T> T notNull(T something) {
        return Optional.ofNullable(something).orElseThrow(() -> new TripFlowValidationError("Argument cannot be null"));
    }

}
