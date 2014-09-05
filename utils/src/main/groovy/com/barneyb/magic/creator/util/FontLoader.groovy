package com.barneyb.magic.creator.util

import java.awt.Font
import java.awt.GraphicsEnvironment

/**
 *
 *
 * @author barneyb
 */
class FontLoader {

    static void fromClasspath(String... paths) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        def cl = FontLoader.classLoader
        paths.each {
            def type
            if (it.endsWith("ttf") || it.endsWith("TTF")) {
                type = Font.TRUETYPE_FONT
            } else {
                throw new IllegalArgumentException("Unknown font type: $it")
            }
            ge.registerFont(Font.createFont(type, cl.getResourceAsStream(it)))
        }
    }

}
