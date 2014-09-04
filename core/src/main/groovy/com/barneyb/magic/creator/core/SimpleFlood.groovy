package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Flood
import groovy.transform.Immutable
import groovy.transform.ToString

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
@Immutable
@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class SimpleFlood implements Flood {

    Color color

    float opacity = 1

    @Override
    boolean isNonOpaque() {
        opacity != 1
    }

}
