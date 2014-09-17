package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface Keyed {

    /**
     * I am a unique identifier for this object, which should be as simple as
     * possible, designed for human consumption, and immutable.
     */
    String getKey()

}
