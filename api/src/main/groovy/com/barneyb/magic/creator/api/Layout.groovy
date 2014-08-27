package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I represent a single way of laying out cards within a theme.
 *
 * @author bboisvert
 */
interface Layout {

    /**
     * I will lay out the given {@link Card} according to the the rules
     * encapsulated by this object.
     */
    SVGDocument layout(Card card)


}