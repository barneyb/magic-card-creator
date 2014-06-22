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
        WHITE_BLUE,
        WHITE_BLACK,
        WHITE_RED,
        WHITE_GREEN,
        BLUE_BLACK,
        BLUE_RED,
        BLUE_GREEN,
        BLACK_RED,
        BLACK_GREEN,
        RED_GREEN

        final int priority = 2

        String getAssetKey() {
            name().toLowerCase()
        }
    }

    int getPriority()

}
