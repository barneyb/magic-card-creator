package com.barneyb.magic.creator.compositor

import com.barneyb.magic.creator.asset.ImageAsset

import java.awt.Dimension

/**
 *
 * @author bboisvert
 */
class CompoundImageAsset implements Renderable, List<ImageAsset> {

    @Delegate
    List<ImageAsset> items

    def CompoundImageAsset(List<ImageAsset> items=[]) {
        this.items = items
    }

    Dimension getSize() {
        def sizes = items*.size
        new Dimension((int) sizes*.width.sum(), (int) sizes*.height.max())
    }

}
