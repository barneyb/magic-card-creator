package com.barneyb.magic.creator.api

import java.awt.Color
import java.awt.Rectangle

/**
 * I represent a single texture used in a theme.  Texture objects may be
 * reused.  Conceptually the opacity, offset, and flooding are redundant as
 * they can be expressed in the image itself, but it's easier to tweak if
 * they are just settings in a config file somewhere.
 */
interface Texture extends Transparent {

    RasterImage getImage()

    boolean isUnderFlooded()

    Flood getUnderFlood()

    boolean isOverFlooded()

    Flood getOverFlood()

    Rectangle getBounds()

    Color getTextColor()

}
