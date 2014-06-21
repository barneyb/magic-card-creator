package com.barneyb.magic.creator.asset
import com.barneyb.magic.creator.compositor.Renderable

import java.awt.*
import java.awt.image.BufferedImage
/**
 *
 * @author bboisvert
 */
interface ImageAsset extends Renderable {

    Dimension getSize()

    BufferedImage asImage()

}