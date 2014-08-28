package com.barneyb.magic.creator.api

import java.awt.*
import java.awt.image.BufferedImage

/**
 *
 * @author bboisvert
 */
interface RasterImage {

    URL getUrl()

    Dimension getSize()

    InputStream getInputStream()

    BufferedImage getImage()

    boolean isExists()

}
