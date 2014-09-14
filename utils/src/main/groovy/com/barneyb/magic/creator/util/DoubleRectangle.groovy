package com.barneyb.magic.creator.util

import groovy.transform.TupleConstructor

import java.awt.geom.Dimension2D
import java.awt.geom.Rectangle2D

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor(includeSuperFields = true)
class DoubleRectangle extends Rectangle2D.Double {

    DoubleRectangle minus(Dimension2D dim) {
        new DoubleRectangle(x, y, width - dim.width, height - dim.height)
    }
}
