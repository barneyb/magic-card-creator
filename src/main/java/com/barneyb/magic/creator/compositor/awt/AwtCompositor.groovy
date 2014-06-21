package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel
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
            (int) rs.titlebar.width - i * rs.large.size.width,
            (int) rs.titlebar.height
        ), model.title)
        drawAsset(g, rs.artwork, model.artwork)
        drawText(g, rs.typebar, model.type)
        final bodyFontSize = rs.small.size.height + 1
        def y = rs.textbox.y
        model.body.each { para ->
            def xOffset = 0
            para.each {
                if (it instanceof RenderableString) {
                    if (it.text == null || it.text.length() == 0) {
                        throw new UnsupportedOperationException("You cannot render empty blocks of body text.")
                    }
                    def s = it.toString()
                    def attr = new AttributedString(s, [
                        (TextAttribute.SIZE): bodyFontSize,
                        (TextAttribute.POSTURE): it.flavor ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR
                    ])
                    def lm = new LineBreakMeasurer(attr.iterator, g.getFontRenderContext())
                    TextLayout l
                    while (lm.position < s.length()) {
                        if (l != null) {
                            y += l.ascent + l.descent + l.leading
                        }
                        l = lm.nextLayout((float) rs.textbox.width - xOffset)
                        l.draw(g, (float) rs.textbox.x + xOffset, (float) y + bodyFontSize * 0.8)
                        xOffset = 0
                    }
                    xOffset = l.advance
                } else {
                    it = (ImageAsset) it
                    drawAsset(g, new Rectangle(new Point((int) rs.textbox.x + xOffset, (int) y), it.size), it)
                    xOffset += it.size.width
                }
            }
            y += bodyFontSize * 2
        }
        if (model.powerToughnessVisible) {
            drawText(g, rs.powertoughness, model.powerToughness, Align.CENTER)
        }
        drawText(g, rs.artist, model.artist)
        drawText(g, rs.footer, model.footer)
        card
    }

    protected void drawAsset(Graphics2D g, Rectangle box, ImageAsset asset) {
        g.drawImage(
            asset.asImage(),
            new AffineTransformOp(AffineTransform.getScaleInstance(box.width / asset.size.width, box.height / asset.size.height), AffineTransformOp.TYPE_BICUBIC),
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
