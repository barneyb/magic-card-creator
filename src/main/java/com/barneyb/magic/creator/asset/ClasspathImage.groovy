package com.barneyb.magic.creator.asset
/**
 *
 * @author bboisvert
 */
class ClasspathImage extends RemoteImage {

    String assetPath

    def ClasspathImage(String assetPath) {
        super(ClasspathImage.classLoader.getResource(assetPath))
        this.assetPath = assetPath
    }

    String toString() {
        '{' + assetPath + '}'
    }

}
