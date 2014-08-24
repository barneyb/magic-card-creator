package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Flood
import groovy.transform.Immutable

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
@Immutable
class SimpleFlood implements Flood {

    Color color

    float opacity = 1

    @Override
    boolean isNonOpaque() {
        opacity != 1
    }

}
