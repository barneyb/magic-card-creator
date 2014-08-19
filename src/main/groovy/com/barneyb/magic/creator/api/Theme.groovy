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

    IconFactory getIconFactory()

    /**
     * I return the Layout for the specified layout type.
     */
    Layout getLayout(LayoutType type)

    /**
     * I return the FuseLayout for this theme.  Note that only the INSTANT and
     * SORCERY LayoutTypes supports fusing, though the two halves can be any
     * combination of the two.
     */
    FuseLayout getFuseLayout()

}
