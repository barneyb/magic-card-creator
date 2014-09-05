package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.RasterImage
import com.barneyb.magic.creator.core.DoubleDimension
import com.barneyb.magic.creator.textlayout.Align
import com.barneyb.magic.creator.textlayout.LayoutUtils
import com.barneyb.magic.creator.util.FontLoader
import groovy.transform.InheritConstructors
import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGDocument

import java.awt.Color
import java.awt.Rectangle
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
/**
 *
 *
 * @author barneyb
 */
@InheritConstructors
class DefaultLayout extends VelocityLayout {

    static final TextBox TITLE_BAR = new TextBox(70, 70, 738, 45, "Matrix", true)
    static final Rectangle ARTWORK = new Rectangle(68, 139, 740, 541)
    static final TextBox TYPE_BAR = new TextBox(72, 706, 730, 40, "Matrix", true)
    static final TextBox TEXTBOX = new TextBox(76, 780, 720, 305, "GaramondNo8")
    static final TextBox POWER_TOUGHNESS = new TextBox(680, 1112, 125, 46, "Goudy Old Style", true)
    static final TextBox ARTIST = new TextBox(135, 1132, 503, 28, "Matrix", true)
    static final TextBox FOOTER = new TextBox(55, 1161, 583, 24, "GaramondNo8")

    static {
        FontLoader.fromClasspath(
            "fonts/Matrix-Bold.ttf",
            "fonts/GaramondNo8-Regular.ttf",
            "fonts/GoudyOldStyle-Regular.ttf",
        )
    }

    LayoutUtils layoutUtils = new LayoutUtils()

    @Override
    void layoutInternal(SVGDocument doc, Card card) {
        SVGGraphics2D g = new SVGGraphics2D(doc)
        def tool = new FrameTool(theme, card)
        def defs = el(doc.rootElement, 'defs', [
            id: "icons"
        ])
        def iconDef = { String idPrefix, Icon it ->
            def icon = it.document.rootElement.cloneNode(true)
            doc.adoptNode(icon)
            el(defs, 'g', [
                id: "$idPrefix-$it.key"
            ]).appendChild(icon)
        }

        TextBox titleBar
        if (card.castingCost == null) {
            titleBar = TITLE_BAR
        } else {
            def cost = tool.costAsIcons
            def totalCostIconWidth = (double) cost*.size*.width.sum(0)

            cost.unique(false).each iconDef.curry("cost")
            def gc = el(doc.rootElement, 'g', [
                id: "casting-cost",
                transform: "translate(${TITLE_BAR.x + TITLE_BAR.width - totalCostIconWidth} $TITLE_BAR.y)"
            ])
            float x = 0
            cost.each {
                def factor = (float) TITLE_BAR.height / it.size.height
                def width = it.size.width * factor
                el(gc, 'use', [
                    'xlink:href': "#cost-$it.key",
                    transform: "translate($x 0) scale($factor)"
                ])
                x += width
            }
            // constrain the title bar to what's left after the cost plus half the average icon width
            def avgCostIconWidth = 1.0 * totalCostIconWidth / cost.size()
            titleBar = TITLE_BAR - new DoubleDimension(totalCostIconWidth + avgCostIconWidth / 2, 0)
        }
        layoutUtils.line(titleBar, card.title, titleBar.textAttributes).draw(g)
        xmlImage(g, ARTWORK, card.artwork)
        // todo: set/rarity
        layoutUtils.line(TYPE_BAR, card.typeParts.join(" ") + (card.subtypeParts ? ' \u2014 ' + card.subtypeParts.join(" ") : ""), TYPE_BAR.textAttributes).draw(g)
        // todo: textbox
        if (card instanceof CreatureCard) {
            layoutUtils.line(POWER_TOUGHNESS, "$card.power/$card.toughness", POWER_TOUGHNESS.textAttributes, Align.CENTER).draw(g)
        }
        layoutUtils.line(ARTIST, card.artwork.artist, ARTIST.textAttributes).draw(g)
        layoutUtils.line(FOOTER, "$card.copyright $card.cardNumber/$card.setCardCount", FOOTER.textAttributes).draw(g)

        g.color = Color.YELLOW
        g.draw(TITLE_BAR)
        g.draw(titleBar)
        g.draw(ARTWORK)
        g.draw(TYPE_BAR)
        g.draw(TEXTBOX)
        g.draw(POWER_TOUGHNESS)
        g.draw(ARTIST)
        g.draw(FOOTER)

        doc.documentElement.appendChild(g.topLevelGroup)
    }

    protected void xmlImage(SVGGraphics2D svgg, Rectangle box, RasterImage ri) {
        def img = ri.image
        def size = ri.size
        def wf = box.width / size.width
        def hf = box.height / size.height
        def f = Math.abs(wf) > Math.abs(hf) ? wf : hf
        svgg.clip = box
        def x = box.x + (box.width - size.width * f) / 2
        def y = box.y + (box.height - size.height * f) / 2
        svgg.drawImage(img, new AffineTransformOp(AffineTransform.getScaleInstance(f, f), AffineTransformOp.TYPE_BICUBIC), (int) x, (int) y)
        svgg.clip = null
    }

    protected Element el(Element parent, String name, Map<String, ?> attrs=[:]) {
        def el = parent.ownerDocument.createElementNS(parent.namespaceURI, name)
        parent.appendChild(elattr(el, attrs))
    }

    protected Element elattr(Element el, Map<String, ?> attrs) {
        attrs.each { n, v ->
            el.setAttributeNS(null, n, v.toString())
        }
        el
    }

}
