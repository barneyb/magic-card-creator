package com.barneyb.magic.creator.compositor

import groovy.transform.Canonical

/**
 * Created by barneyb on 7/27/2014.
 */
@Canonical
class PrintMorph {

    /**
     * The number of degrees to rotate.  Must be a multiple of 90, and should
     * be 0, 90, 180, or 270.
     */
    int rotate

    /**
     * The amount of bleed to add in the x (width) direction.
     */
    float xBleed

    /**
     * The amount of bleed to add in the y (height) direction.
     */
    float yBleed

}
