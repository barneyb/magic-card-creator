package com.barneyb.magic.creator.asset

import java.awt.*

/**
 *
 * @author bboisvert
 */
class ClasspathImage extends RemoteImage {

    String assetPath

    /**
     * This constructor will immediately load the asset, blindly assume it is
     * an ImageIO-readable file, and measure it.  As such, it only works for
     * certain file types and always costs the loading interaction, so should be
     * avoided where possible.
     */
    @Deprecated
    def ClasspathImage(String assetPath) {
        super(ClasspathImage.classLoader.getResource(assetPath))
        this.assetPath = assetPath
    }

    def ClasspathImage(String assetPath, Dimension size) {
        super(ClasspathImage.classLoader.getResource(assetPath), size)
        this.assetPath = assetPath
    }

    String toString() {
        '{' + assetPath + '}'
    }

}
