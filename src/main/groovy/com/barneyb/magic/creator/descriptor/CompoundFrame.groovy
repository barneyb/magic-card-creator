package com.barneyb.magic.creator.descriptor
import groovy.transform.Immutable
/**
 *
 * @author bboisvert
 */
@Immutable
class CompoundFrame implements FrameType {

    FrameBaseType base

    SortedMap<Integer, FrameModifier> modifiers

    @Override
    String getAssetKey() {
        def parts = modifiers.values()*.assetKey
        parts.add(0, base.assetKey)
        parts.join("_")
    }

    @Override
    FrameType plus(FrameModifier mod) {
        if (mod == null) {
            throw new IllegalArgumentException("You cannot add a null modifier to a frame")
        }
        new CompoundFrame(base, modifiers + [(mod.priority): mod] as TreeMap)
    }
}
