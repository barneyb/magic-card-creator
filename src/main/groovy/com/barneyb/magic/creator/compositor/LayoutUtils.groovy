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

    LineLayout line(Dimension2D box, String text, Font font, Align align=Align.LEADING) {
        line(box, text, font.attributes, align)
    }

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

    LineLayout line(Rectangle box, String text, Font font, Align align=Align.LEADING) {
        line(box, text, font.attributes, align)
    }

    LineLayout line(Rectangle box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        line(new Dimension2D(box.width, box.height), text, attrs, align) + new Point2D.Double(box.x, box.y)
    }

}
