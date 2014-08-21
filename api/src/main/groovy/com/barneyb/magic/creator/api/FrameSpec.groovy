package com.barneyb.magic.creator.api

import java.awt.*
import java.util.List

/**
 *
 * @author bboisvert
 */
interface FrameSpec {

    /**
     * I indicate the Texture(s) that should be used for the frame background.
     * I cannot be empty.
     */
    List<Texture> getFrameTextures()

    boolean isMultiFrame()

    /**
     * I indicate the Texture(s) that should be overlaid atop the main frame
     * texture(s).
     */
    List<Texture> getFrameOverlays()

    boolean isOverlaid()

    /**
     * I indicate the Color(s) that should be used for the frame border.  I
     * cannot be empty.
     */
    List<Color> getBorderColors()

    boolean isMultiBorder()

    /**
     * I indicate the Texture(s) that should be used for the textbox background.
     * I cannot be empty.
     */
    List<Texture> getTextboxTextures()

    boolean isMultiTextbox()

    Rarity getRarity()

}