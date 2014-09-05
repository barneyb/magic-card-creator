package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I represent a set of configuration parameters used to create generic card
 * layouts, sans any card-specific information.
 */
interface Theme {

    String getName()

    boolean supports(LayoutType type)

    ColorTheme getColorTheme(ThemedColor color)

    Texture getSemiEnchantmentTexture()

    SymbolIconFactory getSymbolIconFactory()

    /**
     * I will lay out the given {@link Card} according to the the rules
     * of this theme.
     */
    SVGDocument layout(Card card)

}
