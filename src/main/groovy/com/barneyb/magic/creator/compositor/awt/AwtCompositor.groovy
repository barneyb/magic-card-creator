package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.AbilityText
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.CompoundImageAsset
import com.barneyb.magic.creator.compositor.FlavorText
import com.barneyb.magic.creator.compositor.Paragraph
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.Renderable
import com.barneyb.magic.creator.compositor.RenderableText

import javax.imageio.ImageIO
import java.awt.*
import java.awt.font.LineBreakMeasurer
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.text.AttributedString
/**
 *
 * @author bboisvert
 */
@SuppressWarnings("GrMethodMayBeStatic")
class AwtCompositor implements Compositor {

    public static final Font BASE_FONT = Font.decode("Goudy Old Style-bold")

    static protected enum Align {
        LEADING,
        CENTER,
//        TRAILING
    }

    static class RenderCtx {
        final Graphics2D graphics
        Rectangle bounds
        double x
        double y
        float fontSize
        float wrapOffset

        def RenderCtx(Graphics2D g, Rectangle b, float fs, float wo = fs) {
            graphics = g
            bounds = b
            x = bounds.x
            y = bounds.y
            fontSize = fs
            wrapOffset = wo
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

    @Override
    void compose(RenderModel model, RenderSet rs, OutputStream dest) {
        try {
            ImageIO.write(compose(rs, model), "png", dest)
            dest.flush()
        } finally {
            dest.close()
        }
    }

    RenderedImage compose(RenderSet rs, RenderModel model) {
        def card = model.frame.asImage() as BufferedImage
        def g = card.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.font = BASE_FONT
        g.color = Color.BLACK
        int i = 0
        model.cost.reverse().each { it ->
            i += 1
            def r = new Rectangle(
                new Point(
                    (int) rs.titlebar.x + rs.titlebar.width - i * rs.large.size.width,
                    (int) rs.titlebar.y + (rs.titlebar.height - rs.large.size.height) / 2
                ),
                rs.large.size
            )
            drawAsset(g, r, it)
        }
        drawText(g, new Rectangle(
            (int) rs.titlebar.x,
            (int) rs.titlebar.y,
            (int) rs.titlebar.width - (i + 0.5) * rs.large.size.width,
            (int) rs.titlebar.height
        ), model.title)
        drawAsset(g, rs.artwork, model.artwork)
        drawText(g, rs.typebar, model.type)

        g.setClip(rs.textbox)
        def fontSize = (float) rs.small.size.height * 1.15
        g.font = BASE_FONT.deriveFont(fontSize)
        def fm = g.fontMetrics
        def ctx = new RenderCtx(g, rs.textbox, fontSize, fm.ascent + fm.descent)
        model.body.each { line ->
            ctx.XOffset = 0
            line.eachWithIndex { it, itemIdx ->
                render(ctx, it)
            }
            ctx.y += ctx.wrapOffset
        }
        g.setClip(null) // clear the clip

        if (model.powerToughnessVisible) {
            drawText(g, rs.powertoughness, model.powerToughness, Align.CENTER)
        }

        if (model.whiteFooterText) {
            g.color = Color.WHITE
        }
        g.font = BASE_FONT
        drawText(g, rs.artist, model.artist)
        drawText(g, rs.footer, model.footer)
        card
    }

    protected void render(RenderCtx ctx, Renderable it) {
        throw new UnsupportedOperationException("Renderable '${it.getClass().name}' is not supported.")
    }

    protected void render(RenderCtx ctx, Paragraph it) {
        ctx.y -= ctx.wrapOffset * 0.35
    }

    protected void render(RenderCtx ctx, AbilityText it) {
        render(ctx, it, false)
    }

    protected void render(RenderCtx ctx, FlavorText it) {
        render(ctx, it, true)
    }

    protected void render(RenderCtx ctx, RenderableText it, boolean flavor) {
        if (it.text == null || it.text.length() == 0) {
            throw new UnsupportedOperationException("You cannot render empty blocks of body text.")
        }
        def s = it.text
        def attr = new AttributedString(s, [
            (TextAttribute.FONT): BASE_FONT.deriveFont(flavor ? Font.ITALIC : Font.PLAIN, ctx.fontSize)
        ])
        def lm = new LineBreakMeasurer(attr.iterator, ctx.graphics.getFontRenderContext())
        TextLayout l
        while (lm.position < s.length()) {
            if (l != null) {
                ctx.XOffset = 0
                ctx.y += ctx.wrapOffset
            }
            l = lm.nextLayout((float) ctx.bounds.width - ctx.XOffset)
            l.draw(ctx.graphics, (float) ctx.x, (float) ctx.y + l.ascent)
        }
        ctx.XOffset += l.advance
    }

    protected void render(RenderCtx ctx, CompoundImageAsset it) {
        if (ctx.XOffset + it.size.width > ctx.bounds.width) {
            ctx.XOffset = 0
            ctx.y += ctx.wrapOffset
        }
        it.each this.&render.curry(ctx)
    }

    protected void render(RenderCtx ctx, ImageAsset it) {
        drawAsset(ctx.graphics, new Rectangle((int) ctx.x, (int) ctx.y + (ctx.wrapOffset - it.size.height) * 0.5, (int) it.size.width, (int) it.size.height), it)
        ctx.XOffset += it.size.width
    }

    protected void drawAsset(Graphics2D g, Rectangle box, ImageAsset asset) {
        AffineTransformOp transOp = null
        if (asset.size != box.size) {
            transOp = new AffineTransformOp(AffineTransform.getScaleInstance(box.width / asset.size.width, box.height / asset.size.height), AffineTransformOp.TYPE_BICUBIC)
        }
        g.drawImage(
            asset.asImage(),
            transOp,
            (int) box.x,
            (int) box.y
        )
    }

    protected void drawText(Graphics2D g, Rectangle box, String text, Align align=Align.LEADING) {
        def oldFont = g.font
        g.font = BASE_FONT.deriveFont((float) Math.ceil(box.height * 0.85))
        def w = g.fontMetrics.stringWidth(text)
        if (w > box.width) {
            def t = new AffineTransform()
            t.setToScale(box.width / w, 1)
            g.font = g.font.deriveFont(t)
            // sanity check...
//            w = g.fontMetrics.stringWidth(text)
//            if (w > box.width) {
//                throw new IllegalStateException("transforming font for '$text' didn't work right ($w > $box.width).")
//            }
        } else if (align == Align.CENTER && w < box.width) {
            // new bounding box
            box = new Rectangle(
                (int) box.x + (box.width - w) / 2,
                (int) box.y,
                w,
                (int) box.height
            )
        }
        g.drawString(text, (int) box.x, (int) (box.y + box.height * 0.8))
        g.font = oldFont
    }

}
