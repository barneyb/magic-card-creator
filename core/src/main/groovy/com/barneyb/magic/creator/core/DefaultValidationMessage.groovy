package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@SuppressWarnings("GrFinalVariableAccess")
@TupleConstructor
class DefaultValidationMessage<T> implements ValidationMessage<T> {

    static <T> ValidationMessage<T> info(T item, String propertyName=null, String message) {
        new DefaultValidationMessage<T>(ValidationMessage.Level.INFO, item, propertyName, message)
    }

    static <T> ValidationMessage<T> warning(T item, String propertyName=null, String message) {
        new DefaultValidationMessage<T>(ValidationMessage.Level.WARNING, item, propertyName, message)
    }

    static <T> ValidationMessage<T> error(T item, String propertyName=null, String message) {
        new DefaultValidationMessage<T>(ValidationMessage.Level.ERROR, item, propertyName, message)
    }

    final ValidationMessage.Level level

    final T item

    final String propertyName

    final String message

}
