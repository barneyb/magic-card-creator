package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.RasterImage

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

/**
 *
 *
 * @author barneyb
 */
class UrlRasterImage implements RasterImage {

    final URL url

    def UrlRasterImage(URL url) {
        this.url = url
    }

    def UrlRasterImage(URL url, Dimension size) {
        this.url = url
        this.__size = size
    }

    private Dimension __size
    Dimension getSize() {
        if (__size == null) {
            __size = new Dimension(image.width, image.height)
        }
        __size
    }

    private transient BufferedImage __image
    BufferedImage getImage() {
        if (__image == null) {
            try {
                __image = ImageIO.read(getInputStream())
            } catch (RuntimeException re) {
                println "failed to load '" + this + "'"
                throw re
            }
        }
        __image
    }

    private transient byte[] __bytes
    protected byte[] getBytes() {
        if (__bytes == null) {
            __bytes = url.bytes
        }
        __bytes
    }

    @Override
    InputStream getInputStream() {
        new ByteArrayInputStream(bytes)
    }

    boolean isExists() {
        if (url == null) {
            return false
        }
        def s = bytes
        s != null && s.length > 0
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof UrlRasterImage)) return false

        UrlRasterImage that = (UrlRasterImage) o

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
