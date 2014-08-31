package com.barneyb.magic.creator.core

import groovy.transform.TupleConstructor

import java.awt.geom.Dimension2D

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DoubleDimension extends Dimension2D {

    double width
    double height

    @Override
    void setSize(double width, double height) {
        this.width = width
        this.height = height
    }

}
