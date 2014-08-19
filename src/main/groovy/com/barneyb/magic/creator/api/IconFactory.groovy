package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I am in charge of converting Symbols into SVGDocuments that visually
 * represent them.  All documents will contain a single <tt>&lt;g&gt;</tt>
 * element with everything inside.  Further, the base icon shape will always be
 * explicitly filled by the <tt>&lt;svg&gt;</tt> element, and never be stroked.
 * This is intended to allow the result of <tt>getBareIcon</tt> to have it's
 * top-level group extracted for use as a context-free path, though documents
 * returned by all methods will share this structure.
 *
 * @author bboisvert
 */
interface IconFactory {

    /**
     * I construct a standard icon for the symbol as would be found in a card
     * ability cost (flat with a disc).
     */
    SVGDocument getIcon(Symbol symbol)

    /**
     * I construct a icon for the symbol as would be found in a card's casting
     * cost (with a disc and a drop shadow).
     */
    SVGDocument getShadowedIcon(Symbol symbol)

    /**
     * I construct a icon for the symbol without a disc or shadow (i.e., just
     * the stuff that would be found w/in the disk on a normal icon).
     */
    SVGDocument getBareIcon(Symbol symbol)

}
