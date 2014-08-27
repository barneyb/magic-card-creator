package com.barneyb.magic.creator.theme

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
class ClasspathRasterImage extends UrlRasterImage {

    String assetPath

    def ClasspathRasterImage(String assetPath) {
        super(ClasspathRasterImage.classLoader.getResource(assetPath))
        this.assetPath = assetPath
    }

    def ClasspathRasterImage(String assetPath, Dimension size) {
        super(ClasspathRasterImage.classLoader.getResource(assetPath), size)
        this.assetPath = assetPath
    }

    String toString() {
        '{classpath:' + assetPath + '}'
    }

}
