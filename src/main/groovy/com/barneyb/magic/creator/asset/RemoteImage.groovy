package com.barneyb.magic.creator.asset
import groovy.transform.TupleConstructor

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
/**
 *
 * @author bboisvert
 */
@TupleConstructor
class RemoteImage implements ImageAsset {

    URL url

    private transient BufferedImage __image
    protected BufferedImage getImage() {
        if (__image == null) {
            try {
                __image = ImageIO.read(url)
            } catch (RuntimeException re) {
                println "failed to load '" + this + "'"
                throw re
            }
        }
        __image
    }

    @Override
    Dimension getSize() {
        new Dimension(image.width, image.height)
    }

    @Override
    BufferedImage asImage() {
        image
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof RemoteImage)) return false

        RemoteImage that = (RemoteImage) o

        if (url != that.url) return false

        return true
    }

    int hashCode() {
        return url.hashCode()
    }

    @Override
    public String toString() {
        '{' + url + '}'
    }

}
