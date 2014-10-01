package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface CardSetWriter extends Closeable {

    void write(CardSet cardSet)

}
