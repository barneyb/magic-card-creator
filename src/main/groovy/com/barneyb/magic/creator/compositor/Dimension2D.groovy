package com.barneyb.magic.creator.compositor

import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
@TupleConstructor
class Dimension2D extends java.awt.geom.Dimension2D {

    double width
    double height

    @Override
    void setSize(double v, double v2) {
        width = v
        height = v2
    }

}
