package com.barneyb.magic.creator.descriptor

/**
 *
 * @author bboisvert
 */
interface FrameType extends AssetKeyed {

    FrameType plus(FrameModifier mod)

    FrameType minus(FrameModifier mod)

    boolean getWhiteFooterText()
}
