package com.barneyb.magic.creator.descriptor
/**
 *
 * @author bboisvert
 */
enum FrameBaseType implements FrameType {

    ARTIFACT,
    BLACK,
    BLUE,
    GOLD,
    GREEN,
    LAND,
    RED,
    WHITE

    String getAssetKey() {
        name().toLowerCase()
    }

    @Override
    FrameType plus(FrameModifier mod) {
        new CompoundFrame(this, [(mod.priority): mod] as TreeMap)
    }
}