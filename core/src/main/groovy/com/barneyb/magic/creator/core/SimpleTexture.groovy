package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Flood
import com.barneyb.magic.creator.api.RasterImage
import com.barneyb.magic.creator.api.Texture
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class SimpleTexture implements Texture {

    RasterImage image

    float opacity = 1

    Flood overFlood

    Flood underFlood

    Rectangle bounds
    Rectangle getBounds() {
        bounds ?: new Rectangle(new Point(0, 0), image.size)
    }

    @Override
    boolean isNonOpaque() {
        opacity != 1
    }

    @Override
    boolean isOverFlooded() {
        overFlood != null
    }

    @Override
    boolean isUnderFlooded() {
        underFlood != null
    }

}
