package com.github.tripflow.infrastructure.map;

/*
    References:
    ----------

    1.  This class is copied from cargo-clean project. See https://github.com/gushakov/cargo-clean/blob/main/src/main/java/com/github/cargoclean/infrastructure/adapter/map/IgnoreForMapping.java.
    2.  Ignoring methods with MapStruct: https://bit.ly/38T5TfY
 */

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface IgnoreForMapping {
    // use this annotation for methods which should be ignored by MapStruct
}
