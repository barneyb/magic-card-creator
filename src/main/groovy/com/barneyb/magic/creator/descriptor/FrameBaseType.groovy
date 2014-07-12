package com.barneyb.magic.creator.descriptor
/**
 *
 * @author bboisvert
 */
enum FrameBaseType implements FrameType {

    ARTIFACT,
    BLACK(true),
    BLUE,
    GOLD,
    GREEN,
    LAND(true),
    RED,
    WHITE

    final boolean whiteFooterText

    def FrameBaseType(boolean whiteFooterText=false) {
        this.whiteFooterText = whiteFooterText
    }

    String getAssetKey() {
        name().toLowerCase()
    }

    @Override
    FrameType plus(FrameModifier mod) {
        new CompoundFrame(this, [mod])
    }

    @Override
    FrameType minus(FrameModifier mod) {
        // FrameBaseType never has mods, so removing one is always a no-op
        this
    }

}