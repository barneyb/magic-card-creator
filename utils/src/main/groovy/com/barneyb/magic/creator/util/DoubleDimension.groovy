package com.barneyb.magic.creator.util

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.awt.geom.Dimension2D

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode
@ToString(includeNames = false, includePackage = false)
class DoubleDimension extends Dimension2D {

    double width
    double height

    @Override
    void setSize(double width, double height) {
        this.width = width
        this.height = height
    }

}
