package com.barneyb.magic.creator.textlayout

import groovy.transform.Immutable

import java.awt.geom.Point2D

/**
 *
 * @author bboisvert
 */
@Immutable
class LineLayout {

    /**
     * The font size to use for this line (based on box height).
     */
    float fontSize
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
        new LineLayout(fontSize, (float) x + p.x, (float) y + p.y, scale)
    }

    boolean isScaled() {
        scale != 1
    }

}
