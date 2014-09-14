package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Flood
import com.barneyb.magic.creator.api.RasterImage
import com.barneyb.magic.creator.api.Texture
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import java.awt.geom.Rectangle2D

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class SimpleTexture implements Texture {

    RasterImage image

    Float opacity
    float getOpacity() {
        opacity ?: 1
    }

    Flood overFlood

    Flood underFlood

    Rectangle2D bounds
    Rectangle2D getBounds() {
        bounds ?: new Rectangle(new Point(0, 0), image.size)
    }

    Color textColor = Color.BLACK

    @Override
    boolean isNonOpaque() {
        opacity != null && opacity != 1
    }

    @Override
    boolean isOverFlooded() {
        overFlood != null
    }

    @Override
    boolean isUnderFlooded() {
        underFlood != null
    }

    SimpleTexture floodOver(Flood f) {
        new SimpleTexture(image, opacity, f, underFlood)
    }

    SimpleTexture floodUnder(Flood f) {
        new SimpleTexture(image, opacity, overFlood, f)
    }

    SimpleTexture derive(float o) {
        new SimpleTexture(image, o, overFlood, underFlood)
    }

    SimpleTexture plus(SimpleTexture t) {
        new SimpleTexture(
            t.image ?: image,
            t.opacity ?: opacity,
            t.overFlood ?: overFlood,
            t.underFlood ?: underFlood,
            t.@bounds ?: bounds,
            t.textColor ?: textColor
        )
    }

}
