package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I represent a single way of laying out frames and cards, for example
 * "post-2015 planeswalker".
 *
 * @author bboisvert
 */
interface Layout {

    SVGDocument getFrame(FrameSpec fs)

    SVGDocument getCard(FrameSpec fs, Card card)

    SVGDocument getCard(SVGDocument frame, Card card)


}