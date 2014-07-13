package com.barneyb.magic.creator.compositor

import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.awt.geom.Point2D
import java.text.AttributedString

/**
 *
 * @author bboisvert
 */
class LayoutUtils {

    /**
     * Defers to {@link #line(com.barneyb.magic.creator.compositor.Dimension2D, java.lang.String, java.util.Map, com.barneyb.magic.creator.compositor.Align)}.
     */
    LineLayout line(Dimension2D box, String text, Font font, Align align=Align.LEADING) {
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
     * @param attrs The TextAttributes the text will be drawn with.
     * @param align The alignment of the text within the box.
     * @return a LineLayout describing how the text should be laid out w/in the box.
     */
    LineLayout line(Dimension2D box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        if (! attrs.containsKey(TextAttribute.FAMILY)) {
            throw new IllegalArgumentException("You must supply a 'family' attribute to lay out a line of text.")
        }
        def frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        float fontSize = box.height

        // measure at full height
        def font = new Font(attrs + [
            (TextAttribute.SIZE): fontSize,
        ])
        def lm = font.getLineMetrics(text, frc)

        // recalc fontsize assuming linear vertical scaling
        fontSize = fontSize / (lm.ascent + Math.abs(lm.descent)) * box.height
        font = new Font(attrs + [
            (TextAttribute.SIZE): fontSize,
        ])
        lm = font.getLineMetrics(text, frc)

        // do the math
        float y = lm.ascent + (box.height - lm.ascent - Math.abs(lm.descent)) / 2
        float x = 0
        float w = new TextLayout(new AttributedString(text, font.attributes).iterator, frc).advance
        float xScale = 1
        if (align == Align.CENTER && w < box.width) {
            x += (box.width - w) / 2
        }
        if (w > box.width) {
            xScale = box.width / w
        }
        new LineLayout(fontSize, x, y, xScale)
    }

    /**
     * Defers to {@link #line(java.awt.Rectangle, java.lang.String, java.util.Map, com.barneyb.magic.creator.compositor.Align)}.
     */
    LineLayout line(Rectangle box, String text, Font font, Align align=Align.LEADING) {
        line(box, text, font.attributes, align)
    }

    /**
     * Defers to {@link #line(com.barneyb.magic.creator.compositor.Dimension2D, java.lang.String, java.util.Map, com.barneyb.magic.creator.compositor.Align)},
     * and then offsets the coordinates by those in the Rectangle.
     */
    LineLayout line(Rectangle box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        line(new Dimension2D(box.width, box.height), text, attrs, align) + new Point2D.Double(box.x, box.y)
    }



}
