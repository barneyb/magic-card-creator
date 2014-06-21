package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel

import javax.imageio.ImageIO
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
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
        drawText(g, rs.titlebar, model.title)
        model.cost.reverse().eachWithIndex { it, i ->
            def r = new Rectangle(
                new Point(
                    (int) rs.titlebar.x + rs.titlebar.width - (i + 1) * rs.large.size.width,
                    (int) rs.titlebar.y + (rs.titlebar.height - rs.large.size.height) / 2
                ),
                rs.large.size
            )
            drawAsset(g, r, it)
        }
        drawAsset(g, rs.artwork, model.artwork)
        drawText(g, rs.typebar, model.type)
        // body
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
        g.font = g.font.deriveFont((float) Math.ceil(box.height * 0.85))
        if (align == Align.CENTER) {
            // new bounding box
            box = centeredRectangle(g, box, text)
        }
        g.drawString(text, (int) box.x, (int) (box.y + box.height * 0.8))
    }

    protected Rectangle centeredRectangle(Graphics2D g, Rectangle box, String text) {
        def fm = g.getFontMetrics(g.getFont())
        def w = fm.stringWidth(text)
        if (w < box.width) {
            new Rectangle(
                (int) box.x + (box.width - w) / 2,
                (int) box.y,
                w,
                (int) box.height
            )
        } else {
            box
        }
    }

}
