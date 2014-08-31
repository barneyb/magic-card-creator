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
     * I am this icon's unique identifier, and should be both as simple as
     * possible (to support use in logs) but retain sufficient uniqueness
     * to allow implementations to operate on it alone for <tt>equals()</tt>.
     */
    String getKey()

    /**
     * The natural size of the icon, which is primarily a way of accessing the
     * aspect ratio, though may be useful for for semi-raster SVGs.
     */
    Dimension getSize()

    /**
     * The SVGDocument which visually represents this icon.
     */
    SVGDocument getDocument()

}
