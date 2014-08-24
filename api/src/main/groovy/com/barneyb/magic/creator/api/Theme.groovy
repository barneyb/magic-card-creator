package com.barneyb.magic.creator.api

/**
 * I represent a set of configuration parameters used to create generic card
 * layouts, sans any card-specific information.
 */
interface Theme {

    String getName()

    /**
     * I return the ColorTheme for the specified color.
     */
    ColorTheme getColorTheme(ThemedColor color)

    SymbolIconFactory getIconFactory()

    /**
     * I am a frame overlay used to indicate semi-enchantment cards, or null if
     * this theme doesn't support visual semi-enchantment differentiation.
     */
    Texture getSemiEnchantmentTexture()

    /**
     * I return the Layout for the specified layout type.
     */
    Layout getLayout(LayoutType type)

    boolean supports(LayoutType type)

    /**
     * I return the FuseLayout for this theme.  Note that only the INSTANT and
     * SORCERY LayoutTypes supports fusing, though the two halves can be any
     * combination of the two.
     */
    FuseLayout getFuseLayout()

}
