package com.barneyb.magic.creator.api
import org.w3c.dom.svg.SVGDocument

import java.awt.geom.Dimension2D
/**
 *
 *
 * @author barneyb
 */
interface Icon extends Keyed {

    /**
     * The natural size of the icon, which is primarily a way of accessing the
     * aspect ratio, though may be useful for for semi-raster SVGs.
     */
    Dimension2D getSize()

    /**
     * The SVGDocument which visually represents this icon.
     */
    SVGDocument getDocument()

    /**
     * The raw SVG XML which visually represents this icon as a String.
     */
    String getDocumentString()

}
