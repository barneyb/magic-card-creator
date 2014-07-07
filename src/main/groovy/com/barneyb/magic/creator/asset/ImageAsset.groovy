package com.barneyb.magic.creator.asset
import com.barneyb.magic.creator.compositor.Renderable

import java.awt.*
/**
 *
 * @author bboisvert
 */
interface ImageAsset extends Renderable {

    Dimension getSize()

    InputStream getInputStream()

    boolean isExists()

}