package com.barneyb.magic.creator.compositor

import com.barneyb.magic.creator.asset.ImageAsset
/**
 *
 * @author bboisvert
 */
class RenderModel {

    ImageAsset frame

    String title

    List<ImageAsset> cost

    ImageAsset artwork

    String type

    List<Renderable> body

    boolean isPowerToughnessVisible() {
        powerToughness != null
    }

    // might be null
    String powerToughness

    String artist

    String footer

}
