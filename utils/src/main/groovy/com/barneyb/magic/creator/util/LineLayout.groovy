package com.barneyb.magic.creator.util

import groovy.transform.Immutable

import java.awt.Graphics2D
import java.awt.font.TextLayout
import java.awt.geom.Point2D

/**
 *
 * @author bboisvert
 */
@Immutable
class LineLayout {

    /**
     * The font attributes to use for this line.
     */
    TextLayout layout

    /**
     * The x coordinate the baseline should start at.
     */
    float x

    /**
     * The y coordinate the baseline should start at.
     */
    float y

    /**
     * The scaling transform needed in the x direction to fit in the box.
     */
    float scale

    LineLayout plus(Point2D p) {
        new LineLayout(layout, (float) x + p.x, (float) y + p.y, scale)
    }

    boolean isScaled() {
        scale != 1
    }

    void draw(Graphics2D g) {
        layout.draw(g, x, y)
    }
}
