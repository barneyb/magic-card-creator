package com.barneyb.magic.creator.compositor

import com.barneyb.magic.creator.asset.RenderSet

import java.awt.image.RenderedImage
/**
 *
 * @author bboisvert
 */
interface Compositor {

    RenderedImage compose(RenderSet rs, RenderModel model)

}