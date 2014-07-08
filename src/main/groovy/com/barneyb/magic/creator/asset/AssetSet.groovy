package com.barneyb.magic.creator.asset
import com.barneyb.magic.creator.descriptor.AssetKeyed

import java.awt.*
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
        new ClasspathImage(keyed.assetKey, "assets/$renderSet.key/$key/${keyed.assetKey}.$type", size)
    }
}
