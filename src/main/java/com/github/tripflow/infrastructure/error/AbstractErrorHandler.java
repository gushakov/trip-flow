package com.github.tripflow.infrastructure.error;

/*
    References:
    ----------

    1.  Rollback active transaction: https://stackoverflow.com/a/23502214
 */


import lombok.extern.slf4j.Slf4j;

/**
 * This a common parent for all adapters which must share some error handling logic.
 * By default, this error handler will log any exception.
 */
@Slf4j
public abstract class AbstractErrorHandler {

    protected void logError(Exception e) {
        // if we are here it usually means that we may have an unforeseen
        // error which we should probably log for debugging purposes
        log.error(e.getMessage(), e);
    }

}
