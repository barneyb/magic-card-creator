package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I represent the layout of a fuse card.  Note that only non-permanent spells
 * (i.e., instants and sorceries) may be fused, so a given theme will only ever
 * have a single FuseLayout (likely mirroring much of the normal non-fused
 * layout for non-permanent spells).
 *
 * @author bboisvert
 */
interface FuseLayout {

    SVGDocument getFrame(FrameSpec left, FrameSpec right)

    SVGDocument getCard(FrameSpec leftFs, FrameSpec rightFs, Card leftCard, Card rightCard)

    SVGDocument getCard(SVGDocument frame, Card left, Card right)

}