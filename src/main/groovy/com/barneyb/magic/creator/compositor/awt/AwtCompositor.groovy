package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Align
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.LayoutUtils
import com.barneyb.magic.creator.compositor.PrintMorph
import com.barneyb.magic.creator.compositor.RenderModel

import javax.imageio.ImageIO
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.AffineTransformOp
import java.awt.image.RenderedImage
/**
 *
 * @author bboisvert
 */
@SuppressWarnings("GrMethodMayBeStatic")
class AwtCompositor implements Compositor {

    public static final Font BASE_FONT = Font.decode("Goudy Old Style-bold")

    @Override
    void setPrintMorph(PrintMorph morph) {
        throw new UnsupportedOperationException("This compositor does not support print morphing.")
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
        def card = ImageIO.read(model.frame.inputStream)
        def g = card.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.font = new Font(TITLE_FONT)
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
        new LayoutUtils().block(g, rs.textbox, model.body, new Font(BODY_FONT), new Font(FLAVOR_FONT), this.&drawAsset)
        g.setClip(null) // clear the clip

        if (model.powerToughnessVisible) {
            g.font = new Font(POWER_TOUGHNESS_FONT)
            drawText(g, rs.powertoughness, model.powerToughness, Align.CENTER)
        }

        if (model.whiteFooterText) {
            g.color = Color.WHITE
        }
        g.font = new Font(TITLE_FONT)
        drawText(g, rs.artist, model.artist)
        g.font = new Font(BODY_FONT)
        drawText(g, rs.footer, model.footer)
        card
    }

    protected void drawAsset(Graphics2D g, Rectangle2D box, ImageAsset asset) {
        AffineTransformOp transOp = null
        if (asset.size != box.size) {
            transOp = new AffineTransformOp(AffineTransform.getScaleInstance(box.width / asset.size.width, box.height / asset.size.height), AffineTransformOp.TYPE_BICUBIC)
        }
        g.drawImage(
            ImageIO.read(asset.inputStream),
            transOp,
            (int) box.x,
            (int) box.y
        )
    }

    protected void drawText(Graphics2D g, Rectangle box, String text, Align align=Align.LEADING) {
        def ll = new LayoutUtils().line(box, text, BASE_FONT, align)
        def oldFont = g.font
        g.font = BASE_FONT.deriveFont(ll.fontSize)
        if (ll.scaled) {
            g.font = g.font.deriveFont(AffineTransform.getScaleInstance(ll.scale, 1))
        }
        g.drawString(text, (int) ll.x, (int) ll.y)
        g.font = oldFont
    }

}
