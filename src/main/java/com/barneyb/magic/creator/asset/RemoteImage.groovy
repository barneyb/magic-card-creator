package com.barneyb.magic.creator.asset

import groovy.transform.TupleConstructor

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.RenderedImage
/**
 *
 * @author bboisvert
 */
@TupleConstructor
class RemoteImage implements ImageAsset {

    URL url

    private transient RenderedImage __image
    protected RenderedImage getImage() {
        if (__image == null) {
            __image = ImageIO.read(url)
        }
        __image
    }

    @Override
    Dimension getSize() {
        new Dimension(image.width, image.height)
    }

    @Override
    RenderedImage asImage() {
        image
    }
}
