package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.Renderable
import com.barneyb.magic.creator.compositor.RenderableString

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
import java.util.List
/**
 *
 * @author bboisvert
 */
@SuppressWarnings("GrMethodMayBeStatic")
class AwtCompositor implements Compositor {

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
        List<Renderable> line
        int idx

        def RenderCtx(Graphics2D g, Rectangle b, float fs) {
            graphics = g
            bounds = b
            x = bounds.x
            y = bounds.y
            fontSize = fs
            wrapOffset = fs
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
        def ctx = new RenderCtx(g, rs.textbox, (float) rs.small.size.height + 1)
        model.body.each { para ->
            ctx.XOffset = 0
            ctx.line = para
            para.eachWithIndex { it, itemIdx ->
                ctx.idx = itemIdx
                render(ctx, it)
            }
            ctx.y += ctx.wrapOffset * 1.75
        }
        g.setClip(null) // clear the clip

        if (model.powerToughnessVisible) {
            drawText(g, rs.powertoughness, model.powerToughness, Align.CENTER)
        }

        if (model.whiteFooterText) {
            g.color = Color.WHITE
        }
        drawText(g, rs.artist, model.artist)
        drawText(g, rs.footer, model.footer)
        card
    }

    protected void render(RenderCtx ctx, Renderable it) {
        throw new UnsupportedOperationException("Renderable '${it.getClass().name}' is not supported.")
    }

    protected void render(RenderCtx ctx, RenderableString it) {
        if (it.text == null || it.text.length() == 0) {
            throw new UnsupportedOperationException("You cannot render empty blocks of body text.")
        }
        def s = it.toString()
        def attr = new AttributedString(s, [
            (TextAttribute.SIZE): ctx.fontSize,
            (TextAttribute.POSTURE): it.flavor ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR
        ])
        def lm = new LineBreakMeasurer(attr.iterator, ctx.graphics.getFontRenderContext())
        TextLayout l
        while (lm.position < s.length()) {
            if (l != null) {
                ctx.wrapOffset = l.ascent + l.descent + l.leading
                ctx.XOffset = 0
                ctx.y += ctx.wrapOffset
            }
            l = lm.nextLayout((float) ctx.bounds.width - ctx.XOffset)
            l.draw(ctx.graphics, (float) ctx.x, (float) ctx.y + ctx.fontSize * 0.8)
        }
        ctx.XOffset += l.advance
    }

    protected void render(RenderCtx ctx, ImageAsset it) {
        def blockCount = 1
        for (def n : ctx.line.subList(ctx.idx + 1, ctx.line.size())) {
            if (n instanceof ImageAsset) {
                blockCount += 1
            } else {
                break
            }
        }
        if (ctx.XOffset + blockCount * it.size.width >= ctx.bounds.width) {
            ctx.XOffset = 0
            ctx.y += ctx.wrapOffset
        }
        drawAsset(ctx.graphics, new Rectangle(ctx.location, it.size), it)
        ctx.XOffset += it.size.width
    }

    protected void drawAsset(Graphics2D g, Rectangle box, ImageAsset asset) {
        g.drawImage(
            asset.asImage(),
            asset.size == box.size ? null : new AffineTransformOp(AffineTransform.getScaleInstance(box.width / asset.size.width, box.height / asset.size.height), AffineTransformOp.TYPE_BICUBIC),
            (int) box.x,
            (int) box.y
        )
    }

    protected void drawText(Graphics2D g, Rectangle box, String text, Align align=Align.LEADING) {
        def oldFont = g.font
        g.font = oldFont.deriveFont((float) Math.ceil(box.height * 0.85))
        def fm = g.getFontMetrics(g.font)
        def w = fm.stringWidth(text)
        if (w > box.width) {
            def t = new AffineTransform()
            t.setToScale(box.width / w, 1)
            g.font = g.font.deriveFont(t)
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
