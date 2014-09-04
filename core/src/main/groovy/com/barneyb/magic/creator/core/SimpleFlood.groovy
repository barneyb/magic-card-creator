package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.Flood
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.awt.Color

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode
@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class SimpleFlood implements Flood {

    Color color

    float opacity = 1

    @Override
    boolean isNonOpaque() {
        opacity != 1
    }

}
