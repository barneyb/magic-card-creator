package com.barneyb.magic.creator.asset
import com.barneyb.magic.creator.descriptor.AssetKeyed

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

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
            BufferedImage asImage() {
                def path = assetPath
                def stream = RenderSet.classLoader.getResourceAsStream(path)
                if (stream == null) {
                    throw new IllegalArgumentException("No image could be read from the classpath at '$path'.")
                }
                ImageIO.read(stream)
            }

            private String getAssetPath() {
                "assets/$renderSet.key/$key/${keyed.assetKey}.$type"
            }

            String toString() {
                '{' + keyed.assetKey + '}'
            }

            @Override
            int hashCode() {
                assetPath.hashCode()
            }

            @Override
            boolean equals(Object o) {
                getClass().isAssignableFrom(o.getClass()) && assetPath == o.assetPath
            }
        }
    }
}
