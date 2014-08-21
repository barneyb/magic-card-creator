package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I represent the layout of a "fuse" card.  Since only non-permanent spells
 * (i.e., instants and sorceries) may be fused, a given theme will only ever
 * have a single FuseLayout (likely mirroring much of the normal non-fused
 * layout for non-permanent spells).
 *
 * @author bboisvert
 */
interface FuseLayout {

    /**
     * I will lay out the given {@link Card}s according to the the rules
     * encapsulated by this object.
     */
    SVGDocument layout(Card left, Card right)

}