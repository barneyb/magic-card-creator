package com.barneyb.magic.creator.util

import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGDocument

/**
 *
 *
 * @author barneyb
 */
class SvgUtils {

    static Element withGraphics(Closure work) {
        withGraphics(XmlUtils.create(), work, false)
    }

    static Element withGraphics(SVGDocument doc, Closure work, boolean overlay=true) {
        SVGGraphics2D g = new SVGGraphics2D(doc)
        work(g)
        if (overlay) {
            doc.rootElement.appendChild(g.topLevelGroup)
        }
        g.topLevelGroup
    }

}