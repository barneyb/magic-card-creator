package com.barneyb.magic.creator.util

import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.Element

/**
 *
 *
 * @author barneyb
 */
class SvgUtils {

    static Element withGraphics(Closure work) {
        def doc = XmlUtils.create()
        SVGGraphics2D g = new SVGGraphics2D(doc)
        work(g)
        g.topLevelGroup
    }
}
