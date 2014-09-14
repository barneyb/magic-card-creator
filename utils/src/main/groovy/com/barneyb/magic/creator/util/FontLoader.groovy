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
        def cl = FontLoader.classLoader
        fromUrl(paths.collect(cl.&getResource))
    }

    static void fromClasspath(List<String> paths) {
        def cl = FontLoader.classLoader
        fromUrl(paths.collect(cl.&getResource))
    }

    static void fromUrl(List<URL> urls) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        urls.each {
            def type
            if (it.path.endsWith("ttf") || it.path.endsWith("TTF")) {
                type = Font.TRUETYPE_FONT
            } else {
                throw new IllegalArgumentException("Unknown font type: $it.path")
            }
            ge.registerFont(Font.createFont(type, it.newInputStream()))
        }
    }

}
