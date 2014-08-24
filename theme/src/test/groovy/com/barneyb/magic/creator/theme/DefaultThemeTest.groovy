package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.ThemedColor
import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class DefaultThemeTest {

    @Test
    void d() {
        def ct = new DefaultTheme().getColorTheme(ThemedColor.WHITE)
        println ct.baseColor
        println ct.frameTexture
        println ct.frameTexture.image
        println ct.frameTexture.opacity
        println ct.barTexture

        ct = new DefaultTheme().getColorTheme(ThemedColor.RED)
        println ct.baseColor
        println ct.frameTexture
        println ct.frameTexture.image
        println ct.frameTexture.opacity
        println ct.barTexture
    }

}
