package com.barneyb.magic.creator.api
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
interface SymbolIconFactory {

    /**
     * I construct a standard icon for the symbol as would be found in a card
     * ability cost (flat with a disc).
     */
    Icon getIcon(Symbol symbol)

    /**
     * I construct a icon for the symbol as would be found in a card's casting
     * cost (with a disc and a drop shadow).
     */
    Icon getShadowedIcon(Symbol symbol)

    /**
     * I construct a icon for the symbol without a disc or shadow (i.e., just
     * the stuff that would be found w/in the disk on a normal icon).
     */
    Icon getBareIcon(Symbol symbol)

    /**
     * I do the same as {@link #getIcon(com.barneyb.magic.creator.api.Symbol)},
     * but with a {@link SymbolGroup}.
     */
    IconGroup getIcons(SymbolGroup symbols)

    /**
     * I do the same as {@link #getShadowedIcon(com.barneyb.magic.creator.api.Symbol)},
     * but with a {@link SymbolGroup}.
     */
    IconGroup getShadowedIcons(SymbolGroup symbols)

    /**
     * I do the same as {@link #getBareIcon(com.barneyb.magic.creator.api.Symbol)},
     * but with a {@link SymbolGroup}.
     */
    IconGroup getBareIcons(SymbolGroup symbols)

}
