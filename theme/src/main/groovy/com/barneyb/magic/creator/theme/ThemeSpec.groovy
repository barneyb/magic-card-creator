package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ThemedColor

/**
 *
 *
 * @author barneyb
 */
class ThemeSpec {

    String name
    LibrarySpec library
    String icons
    Map<LayoutType, String> layouts
    TextureSpec semiEnchantment
    Map<ThemedColor, ColorThemeSpec> colors

}
