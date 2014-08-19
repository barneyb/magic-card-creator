package com.barneyb.magic.creator.api

import java.awt.*

/**
 * I represent the info related to a specific color w/in this theme.  Note
 * that 'color' is used loosely; gold, artifact, and land are all treated
 * as colors as far as themes are concerned.
 * @author bboisvert
 */
interface ColorTheme {

    /**
     * I am the name of the color, which will be one of: white, blue, black,
     * red, green, gold, artifact, or land.
     */
    String getName()

    /**
     * I am the base color of this theme, at full saturation.
     */
    Color getBaseColor()

    /**
     * I am used for the card frame.
     */
    Texture getFrameTexture()

    /**
     * I am used for the two or three bars on a card (title, type, and
     * optionally power/toughness).
     */
    Texture getBarTexture()

    /**
     * I am used for the textbox of non-mana-producing cards.
     */
    Texture getTextboxTexture()

    /**
     * I am used for the textbox of mana-producing cards.
     */
    Texture getManaTextboxTexture()

    /**
     * I am a frame overlay used to indicate semi-enchantment cards.
     */
    Texture getSemiEnchantmentTexture()

}