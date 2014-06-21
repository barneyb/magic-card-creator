package com.barneyb.magic.creator.asset
import com.barneyb.magic.creator.descriptor.AssetKeyed

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.RenderedImage

/**
 *
 * @author bboisvert
 */
class AssetSet {

    RenderSet renderSet

    String key

    Dimension size

    String type

    ImageAsset getImageAsset(AssetKeyed keyed) {
        new ImageAsset() {
            @Override
            Dimension getSize() {
                AssetSet.this.size
            }

            @Override
            RenderedImage asImage() {
                def path = "assets/$renderSet.key/$key/${keyed.assetKey}.$type"
                def stream = RenderSet.classLoader.getResourceAsStream(path)
                if (stream == null) {
                    throw new IllegalArgumentException("No image could be read from the classpath at '$path'.")
                }
                ImageIO.read(stream)
            }
        }
    }
}
