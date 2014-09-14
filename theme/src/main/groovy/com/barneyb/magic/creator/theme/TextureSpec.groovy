package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Flood

import java.awt.Color
import java.awt.geom.Rectangle2D

/**
 *
 *
 * @author barneyb
 */
class TextureSpec {
    String base
    URL image
    Float opacity
    Flood over
    Flood under
    Rectangle2D bounds
    Color text
}
