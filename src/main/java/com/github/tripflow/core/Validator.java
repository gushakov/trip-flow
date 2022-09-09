package com.github.tripflow.core;

import java.util.Optional;

public class Validator {

    public static <T> T notNull(T something){
        return Optional.ofNullable(something).orElseThrow(IllegalArgumentException::new);
    }

}
