package com.barneyb.magic.creator.asset
import com.barneyb.magic.creator.descriptor.AssetKeyed
import com.barneyb.magic.creator.descriptor.CompoundFrame
import com.barneyb.magic.creator.descriptor.FrameModifier
/**
 *
 * @author bboisvert
 */
class FrameAssetSet extends AssetSet {

    @Override
    ImageAsset getImageAsset(AssetKeyed keyed) {
        def asset = super.getImageAsset(keyed)
        if (keyed instanceof CompoundFrame && ! asset.exists) {
            def dual = keyed.modifiers.find {
                it instanceof FrameModifier.Dual
            }
            if (dual != null) {
                keyed -= dual
                asset = super.getImageAsset(keyed)
            }
        }
        if (keyed instanceof CompoundFrame && ! asset.exists) {
            def type = keyed.modifiers.find {
                it instanceof FrameModifier.Type
            }
            if (type == FrameModifier.Type.ENCHANTMENT_CREATURE) {
                keyed -= type
                keyed += FrameModifier.Type.CREATURE
                asset = super.getImageAsset(keyed)
            }
        }
        asset
    }
}