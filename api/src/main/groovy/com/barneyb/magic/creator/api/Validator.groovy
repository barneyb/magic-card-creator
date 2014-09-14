package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface Validator<T> {

    Collection<ValidationMessage<T>> validate(T item)

}
