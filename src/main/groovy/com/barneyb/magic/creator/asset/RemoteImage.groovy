package com.barneyb.magic.creator.asset

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
/**
 *
 * @author bboisvert
 */
class RemoteImage implements ImageAsset {

    final URL url
    final Dimension size

    /**
     * This constructor will immediately request the URL, blindly assume it is
     * an ImageIO-readable file, and measure it.  As such, it only works for
     * certain file types and always costs the remote interaction, so should be
     * avoided where possible.
     */
    @Deprecated
    RemoteImage(URL url) {
        this.url = url
        this.size = new Dimension(image.width, image.height)
    }

    RemoteImage(URL url, Dimension size) {
        this.url = url
        this.size = size
    }

    private transient BufferedImage __image
    protected BufferedImage getImage() {
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
