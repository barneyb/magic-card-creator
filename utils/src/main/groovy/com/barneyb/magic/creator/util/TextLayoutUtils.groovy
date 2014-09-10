package com.barneyb.magic.creator.util

import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.awt.font.TransformAttribute
import java.awt.geom.AffineTransform
import java.awt.geom.Dimension2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.text.AttributedString

/**
 *
 *
 * @author barneyb
 */
class TextLayoutUtils {

    // blindly assume we'll always use antialiased fractional-pixel rendering
    public static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON)

    public static final String ALL_ALPHANUMERICS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"

    /**
     * Defers to {@link #line(java.awt.geom.Dimension2D, java.lang.String, java.util.Map, Align)}.
     */
    static LineLayout line(Dimension2D box, String text, Font font, Align align=Align.LEADING) {
        line(box, text, font.attributes, align)
    }

    /**
     * Measures the specified text, according to the provided layout instructions,
     * and returns a a LineLayout object describing how to draw the text so that
     * it will be entirely contained w/in the passed box.  The text will be sized
     * to take up the whole height of the box, positioned horizontally within
     * the box according to the specified Align value, and finally scaled in the
     * horizontal direction to prevent overrun.
     *
     * @param box The dimensions of the box the text must fit within.
     * @param text The text to be draw.
     * @param attrs The TextAttributes the text will be drawn with.  Note that
     *  a passed font size, if any, will be ignored.
     * @param align The alignment of the text within the box.
     * @return a LineLayout describing how the text should be laid out w/in the box.
     */
    static LineLayout line(Dimension2D box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        if (! attrs.containsKey(TextAttribute.FAMILY)) {
            throw new IllegalArgumentException("You must supply a 'family' attribute to lay out a line of text.")
        }
        float fontSize = fontSizeForHeight(box.height, text, attrs, false)
        def font = new Font(attrs + [
            (TextAttribute.SIZE): fontSize,
        ])
        def lm = font.getLineMetrics(text, FONT_RENDER_CONTEXT)

        // do the math
        float y = lm.ascent + (box.height - lm.ascent - Math.abs(lm.descent)) / 2
        float x = 0
        def layout = new TextLayout(new AttributedString(text, font.attributes).iterator, FONT_RENDER_CONTEXT)
        float w = layout.advance
        float xScale = 1
        if (align == Align.CENTER && w < box.width) {
            x += (box.width - w) / 2
        } else if (w > box.width || align == Align.STRETCH) {
            xScale = box.width / w
        }
        if (xScale != 1) {
            layout = new TextLayout(new AttributedString(text, font.attributes + [
                (TextAttribute.TRANSFORM): new TransformAttribute(AffineTransform.getScaleInstance(xScale, 1))
            ]).iterator, FONT_RENDER_CONTEXT)
        }
        new LineLayout(layout, x, y, xScale)
    }

    /**
     * I calculate and return the proper font size to get text with the specific
     * height according to the passed TextAttributes, optionally including
     * leading.  For single-line text strings, you probably don't want to use
     * leading, but for multi-line (including wrapped) strings, you definitely
     * would.
     *
     * @param height The desired line height.
     * @param text The text to be drawn.  If null or omitted, all the ASCII
     *  letters and numbers will be used.
     * @param attrs The TextAttributes that will be used to render the text.
     * @param includeLeading Whether to allow space for the leading between
     *  lines, or just the visible height (ascent + descent) of the text.
     * @return The font size to use to get text rendered at the requested height.
     */
    static float fontSizeForHeight(double height, String text=null, Map<TextAttribute, ?> attrs, boolean includeLeading) {
        if (text == null) {
            text = ALL_ALPHANUMERICS
        }
        float fontSize = height
        // measure at full height
        def font = new Font(attrs + [
            (TextAttribute.SIZE): fontSize,
        ])
        def lm = font.getLineMetrics(text, FONT_RENDER_CONTEXT)
        // recalc fontsize assuming linear vertical scaling
        fontSize / (lm.ascent + Math.abs(lm.descent) + (includeLeading ? lm.leading : 0f)) * height
    }


    /**
     * Defers to {@link #line(java.awt.Rectangle, java.lang.String, java.util.Map, Align)}.
     */
    static LineLayout line(Rectangle2D box, String text, Font font, Align align=Align.LEADING) {
        line(box, text, font.attributes, align)
    }

    /**
     * Defers to {@link #line(java.awt.geom.Dimension2D, java.lang.String, java.util.Map, Align)},
     * and then offsets the coordinates by those in the Rectangle.
     */
    static LineLayout line(Rectangle2D box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        line(new DoubleDimension(box.width, box.height), text, attrs, align) + new Point2D.Double(box.x, box.y)
    }

}
