package com.github.tripflow.infrastructure.error;

/*
    References:
    ----------

    1.  Rollback active transaction: https://stackoverflow.com/a/23502214
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * This a common parent for all adapters which must share some error handling logic.
 * This error handler will log any exception, and it will try rollback any active transaction.
 *
 * @see TransactionInterceptor#currentTransactionStatus()
 */
@Slf4j
public abstract class AbstractErrorHandler {


    protected void logErrorAndRollback(Exception e) {

        // if we are here it usually means that we may have an unforeseen
        // error which we should probably log for debugging purposes
        log.error(e.getMessage(), e);

        // we need to roll back any active transaction
        try {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        } catch (NoTransactionException nte) {
            // do nothing if not running in a transactional context
        }

    }

}
