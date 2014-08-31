package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
interface Icon {

    /**
     * I indicate which icon this is, and am primarily for logging purposes.
     */
    String getKey()

    /**
     * The natural size of the icon, which is usually uninteresting except as
     * a way of accessing the aspect ratio.
     */
    Dimension getSize()

    /**
     * The SVGDocument which visually represents this icon.
     */
    SVGDocument getDocument()

}
