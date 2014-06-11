package com.barneyb.magic.creator.asset

import com.barneyb.magic.creator.compositor.Renderable

import java.awt.*
import java.awt.image.RenderedImage

/**
 *
 * @author bboisvert
 */
interface ImageAsset extends Renderable {

    Dimension getSize()

    RenderedImage asImage()

}