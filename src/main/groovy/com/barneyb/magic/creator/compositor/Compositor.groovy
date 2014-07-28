package com.barneyb.magic.creator.compositor
import com.barneyb.magic.creator.asset.RenderSet

import java.awt.font.TextAttribute
/**
 *
 * @author bboisvert
 */
interface Compositor {

    public static final Map<TextAttribute, ?> TITLE_FONT = [
        (TextAttribute.FAMILY)      : "Matrix",
        (TextAttribute.WEIGHT)      : TextAttribute.WEIGHT_BOLD,
    ]
    public static final Map<TextAttribute, ?> BODY_FONT = [
        (TextAttribute.FAMILY)      : "Garamond",
        (TextAttribute.WEIGHT)      : TextAttribute.WEIGHT_REGULAR,
    ]
    public static final Map<TextAttribute, ?> FLAVOR_FONT = [
        (TextAttribute.FAMILY)      : "Garamond italic",
        (TextAttribute.WEIGHT)      : TextAttribute.WEIGHT_REGULAR,
    ]
    public static final Map<TextAttribute, ?> POWER_TOUGHNESS_FONT = [
        (TextAttribute.FAMILY)      : "Goudy Old Style",
        (TextAttribute.WEIGHT)      : TextAttribute.WEIGHT_BOLD,
    ]

    /**
     * I compose the passed RenderModel and RenderSet and send the result to the
     * supplied OutputStream.  The stream should be flushed and closed by the
     * compose method before returning.
     */
    void compose(RenderModel model, RenderSet rs, OutputStream dest)

    void setPrintMorph(PrintMorph morph)

}