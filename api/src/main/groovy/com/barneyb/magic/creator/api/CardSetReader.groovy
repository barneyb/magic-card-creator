package com.barneyb.magic.creator.api

/**
 * I read information from a given resource, and create a CardSet based on the
 * data.
 */
interface CardSetReader extends Closeable {

    CardSet read()

}
