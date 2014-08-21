package com.barneyb.magic.creator.api

import java.awt.Dimension
import java.awt.image.BufferedImage

/**
 *
 * @author bboisvert
 */
interface RasterImage {

    Dimension getSize()

    InputStream getInputStream()

    BufferedImage getImage()

    boolean isExists()

}
