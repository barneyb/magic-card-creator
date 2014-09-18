package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.api.Validator
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
abstract class BaseValidator<T> implements Validator<T> {

    @TupleConstructor
    static class Ctx<T> {
        T item
        String prop
        Collection<ValidationMessage<T>> messages = []

        void error(String prop=this.prop, String msg) {
            messages.add(DefaultValidationMessage.error(item, prop, msg))
        }

        void warning(String prop=this.prop, String msg) {
            messages.add(DefaultValidationMessage.warning(item, prop, msg))
        }

        void info(String prop=this.prop, String msg) {
            messages.add(DefaultValidationMessage.info(item, prop, msg))
        }

    }

    protected void nullOrEmpty(Closure emit, String label, String value) {
        if (value == null) {
            emit("$label shouldn't be null")
        } else if (value.allWhitespace) {
            emit("$label shouldn't be empty (or all whitespace)")
        }
    }

}
