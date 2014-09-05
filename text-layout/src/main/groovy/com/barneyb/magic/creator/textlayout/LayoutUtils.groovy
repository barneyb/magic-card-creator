package com.barneyb.magic.creator.textlayout
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.BodyText
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.IconGroup
import com.barneyb.magic.creator.api.NonNormativeText
import com.barneyb.magic.creator.api.RulesText
import com.barneyb.magic.creator.core.DoubleDimension

import java.awt.Font
import java.awt.Graphics2D
import java.awt.Point
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.font.LineBreakMeasurer
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
 * @author bboisvert
 */
class LayoutUtils {

    // blindly assume we'll always use antialiased fractional-pixel rendering
    public static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON)

    public static final String ALL_ALPHANUMERICS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"

    /**
     * The amount of spacing to be added around icons, as a fraction of the
     * icon's natural width.
     */
    public static final float ICON_SPACING = 0.07

    /**
     * Defers to {@link #line(java.awt.geom.Dimension2D, java.lang.String, java.util.Map, Align)}.
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
     * @param attrs The TextAttributes the text will be drawn with.  Note that
     *  a passed font size, if any, will be ignored.
     * @param align The alignment of the text within the box.
     * @return a LineLayout describing how the text should be laid out w/in the box.
     */
    LineLayout line(Dimension2D box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
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
        }
        if (w > box.width) {
            xScale = box.width / w
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
    float fontSizeForHeight(double height, String text=null, Map<TextAttribute, ?> attrs, boolean includeLeading) {
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
    LineLayout line(Rectangle2D box, String text, Font font, Align align=Align.LEADING) {
        line(box, text, font.attributes, align)
    }

    /**
     * Defers to {@link #line(java.awt.geom.Dimension2D, java.lang.String, java.util.Map, Align)},
     * and then offsets the coordinates by those in the Rectangle.
     */
    LineLayout line(Rectangle2D box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        line(new DoubleDimension(box.width, box.height), text, attrs, align) + new Point2D.Double(box.x, box.y)
    }

    static class RenderCtx {
        final Graphics2D graphics
        final Rectangle2D bounds
        float x
        float y
        Font rulesFont
        Font nonNormativeFont
        float fontSize
        float wrapOffset
        final boolean measuring
        int paragraphCount = 0
        float paragraphBreakSize
        Closure drawIcon

        def RenderCtx(Graphics2D g, Rectangle2D b, float fs, float wo, boolean m) {
            graphics = g
            bounds = b
            x = bounds.x
            y = bounds.y
            fontSize = fs
            wrapOffset = wo
            paragraphBreakSize = wrapOffset * 0.65
            measuring = m
        }

        double getXOffset() {
            x - bounds.x
        }

        void setXOffset(double xOffset) {
            x = bounds.x + xOffset
        }

        Point getLocation() {
            new Point((int) x, (int) y)
        }
    }

    void block(Graphics2D g, Rectangle2D box, List<List<BodyItem>> items, Map<TextAttribute, ?> bodyAttrs, Map<TextAttribute, ?> flavorAttrs, Closure drawAsset) {
        block(g, box, items, new Font(bodyAttrs), new Font(flavorAttrs), drawAsset)
    }

    void block(Graphics2D g, Rectangle2D box, List<List<BodyItem>> items, Font rulesFont, Font nonNormativeFont, Closure drawIcon) {
        def fontSize = fontSizeForHeight(box.height / 7, ALL_ALPHANUMERICS, rulesFont.attributes, true)
        RenderCtx rctx
        for (int _i = 0; _i < 5; _i++) {
            // measure first
            g.font = rulesFont.deriveFont(fontSize)
            def fm = g.fontMetrics
            def mctx = new RenderCtx(g, box, fontSize, fm.ascent + fm.descent, true)
            mctx.rulesFont = rulesFont
            mctx.nonNormativeFont = nonNormativeFont
            items.each { line ->
                mctx.XOffset = 0
                line.each this.&render.curry(mctx)
                mctx.y += mctx.wrapOffset
            }
            // check how we did
            int extraSpace = box.y + box.height - mctx.y
            rctx = new RenderCtx(g, box, fontSize, fm.ascent + fm.descent, false)
            if (extraSpace >= 0) {
                // vertically center
                rctx.y += Math.floor(extraSpace / 2.5)
                break
            } else {
                extraSpace *= -1
                def maxSpaceReduction = mctx.paragraphCount * mctx.paragraphBreakSize * 0.5
                if (extraSpace <= maxSpaceReduction) {
                    // reduce paragraph breaks and done
                    rctx.paragraphBreakSize -= extraSpace / mctx.paragraphCount
                    break
                } else {
                    // shrink it down and repeat
                    fontSize = (float) mctx.fontSize - (extraSpace - maxSpaceReduction) / 10 // the '10' is magic. I don't know what it represents.
                }
            }
        }
        assert rctx != null
        rctx.drawIcon = drawIcon
        rctx.rulesFont = rulesFont
        rctx.nonNormativeFont = nonNormativeFont
        // draw it
        items.each { line ->
            rctx.XOffset = 0
            line.each this.&render.curry(rctx)
            rctx.y += rctx.wrapOffset
        }
    }

    protected void render(RenderCtx ctx, BodyItem it) {
        throw new UnsupportedOperationException("BodyItem '${it.getClass().name}' is not supported.")
    }

    protected void render(RenderCtx ctx, RulesText it) {
        render(ctx, it, ctx.rulesFont)
    }

    protected void render(RenderCtx ctx, NonNormativeText it) {
        render(ctx, it, ctx.nonNormativeFont)
    }

    protected void render(RenderCtx ctx, BodyText it, Font font) {
        if (it.text == null || it.text.length() == 0) {
            throw new UnsupportedOperationException("You cannot render empty blocks of body text.")
        }
        def s = it.text
        def attr = new AttributedString(s, font.deriveFont(ctx.fontSize).attributes)
        def lm = new LineBreakMeasurer(attr.iterator, MagicBreakIteratorProvider.lineInstance, ctx.graphics.getFontRenderContext())
        TextLayout l
        while (lm.position < s.length()) {
            if (l != null || ctx.XOffset >= ctx.bounds.width) {
                ctx.XOffset = 0
                ctx.y += ctx.wrapOffset
            }
            l = lm.nextLayout((float) ctx.bounds.width - ctx.XOffset)
            if (! ctx.measuring) {
                l.draw(ctx.graphics, (float) ctx.x, (float) ctx.y + l.ascent)
            }
        }
        assert l != null
        ctx.XOffset += l.advance
    }

    protected void render(RenderCtx ctx, IconGroup it) {
        //noinspection GroovyAssignabilityCheck
        if (ctx.XOffset + it.collect {
            it.size.width * (1 + ICON_SPACING)
        }.sum() > ctx.bounds.width) {
            ctx.XOffset = 0
            ctx.y += ctx.wrapOffset
        }
        it.each this.&render.curry(ctx)
    }

    protected void render(RenderCtx ctx, Icon it) {
        float factor = ctx.wrapOffset * 0.75 / it.size.height

        def width = it.size.width * factor
        def height = it.size.height * factor
        if (! ctx.measuring) {
            ctx.drawIcon(
                ctx.graphics,
                new Rectangle2D.Double(
                    ctx.x + width * ICON_SPACING / 2,
                    ctx.y + (ctx.wrapOffset - height) * 0.5,
                    width,
                    height
                ) {
                    Dimension2D getSize() {
                        new DoubleDimension(width, height)
                    }
                },
                it
            )
        }
        ctx.XOffset += width * (1 + ICON_SPACING)
    }



}
