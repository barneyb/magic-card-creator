package com.barneyb.magic.creator.textlayout
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.BodyText
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.IconGroup
import com.barneyb.magic.creator.api.LineBreak
import com.barneyb.magic.creator.api.NonNormativeText
import com.barneyb.magic.creator.api.RulesText
import com.barneyb.magic.creator.util.DoubleDimension
import com.barneyb.magic.creator.util.TextLayoutUtils

import java.awt.Font
import java.awt.Graphics2D
import java.awt.Point
import java.awt.font.LineBreakMeasurer
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.awt.geom.Dimension2D
import java.awt.geom.Rectangle2D
import java.text.AttributedString
/**
 *
 * @author bboisvert
 */
class LayoutUtils extends TextLayoutUtils {

    /**
     * The amount of spacing to be added around icons, as a fraction of the
     * icon's natural width.
     */
    public static final float ICON_SPACING = 0.07

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
            paragraphBreakSize = wrapOffset * 0.35
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

        float getParagraphOffset() {
            wrapOffset + paragraphBreakSize
        }

    }

    void block(Graphics2D g, Rectangle2D box, List<List<BodyItem>> items, Map<TextAttribute, ?> bodyAttrs, Map<TextAttribute, ?> flavorAttrs, Closure drawAsset) {
        block(g, box, items, new Font(bodyAttrs), new Font(flavorAttrs), drawAsset)
    }

    void block(Graphics2D g, Rectangle2D box, List<List<BodyItem>> items, Font rulesFont, Font nonNormativeFont, Closure drawIcon) {
        def fontSize = fontSizeForHeight(box.height / 6, ALL_ALPHANUMERICS, rulesFont.attributes, true)
        RenderCtx rctx
        for (int _i = 0; _i < 10; _i++) {
            // measure first
            g.font = rulesFont.deriveFont(fontSize)
            def fm = g.fontMetrics
            def mctx = new RenderCtx(g, box, fontSize, fm.ascent + fm.descent, true)
            mctx.rulesFont = rulesFont
            mctx.nonNormativeFont = nonNormativeFont
            items.each { para ->
                mctx.XOffset = 0
                para.each this.&render.curry(mctx)
                mctx.y += mctx.paragraphOffset
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
                def maxSpaceReduction = (mctx.paragraphCount - 1) * mctx.paragraphBreakSize * 0.5
                if (extraSpace <= maxSpaceReduction) {
                    // reduce paragraph breaks and done
                    rctx.paragraphBreakSize -= extraSpace / (mctx.paragraphCount - 1)
                    break
                } else {
                    // shrink it down and repeat
                    fontSize = (float) mctx.fontSize - (extraSpace - maxSpaceReduction) / 25 // this number is magic. I don't know what it represents.
                }
            }
        }
        assert rctx != null
        rctx.drawIcon = drawIcon
        rctx.rulesFont = rulesFont
        rctx.nonNormativeFont = nonNormativeFont
        // draw it
        items.each { para ->
            rctx.XOffset = 0
            para.each this.&render.curry(rctx)
            rctx.y += rctx.paragraphOffset
        }
    }

    protected void render(RenderCtx ctx, BodyItem it) {
        throw new UnsupportedOperationException("BodyItem '${it.getClass().name}' is not supported.")
    }

    protected void render(RenderCtx ctx, LineBreak it) {
        ctx.XOffset = 0
        ctx.y += ctx.wrapOffset
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
