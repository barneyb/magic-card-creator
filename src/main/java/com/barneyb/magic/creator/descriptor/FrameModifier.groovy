package com.barneyb.magic.creator.descriptor
/**
 *
 * @author bboisvert
 */
interface FrameModifier extends AssetKeyed {

    static enum Type implements FrameModifier {
        CREATURE,
        ENCHANTMENT_CREATURE

        final int priority = 1

        String getAssetKey() {
            name().toLowerCase()
        }
    }

    static enum Dual implements FrameModifier {
        BLUE_BLACK,
        BLUE_RED,
        GREEN_BLACK,
        GREEN_BLUE,
        GREEN_RED,
        RED_BLACK,
        WHITE_BLACK,
        WHITE_BLUE,
        WHITE_GREEN,
        WHITE_RED,

        final int priority = 2

        String getAssetKey() {
            name().toLowerCase()
        }
    }

    int getPriority()

}
