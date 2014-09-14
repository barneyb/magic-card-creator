package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DefaultValidationMessage<T> implements ValidationMessage<T> {

    static <T> ValidationMessage<T> info(T item, String message) {
        new DefaultValidationMessage<T>(ValidationMessage.Level.INFO, item, message)
    }

    static <T> ValidationMessage<T> warning(T item, String message) {
        new DefaultValidationMessage<T>(ValidationMessage.Level.WARNING, item, message)
    }

    static <T> ValidationMessage<T> error(T item, String message) {
        new DefaultValidationMessage<T>(ValidationMessage.Level.ERROR, item, message)
    }

    ValidationMessage.Level level

    T item

    String message

}
