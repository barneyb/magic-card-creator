package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface ValidationMessage<T> {

    enum Level {
        INFO,
        WARNING,
        ERROR
    }

    T getItem()

    Level getLevel()

    String getMessage()

}
