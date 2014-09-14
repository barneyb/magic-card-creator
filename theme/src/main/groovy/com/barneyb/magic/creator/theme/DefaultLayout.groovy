package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.RasterImage
import com.barneyb.magic.creator.textlayout.LayoutUtils
import com.barneyb.magic.creator.util.Align
import com.barneyb.magic.creator.util.DoubleDimension
import groovy.transform.InheritConstructors
import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGDocument

import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.AffineTransformOp
/**
 *
 *
 * @author barneyb
 */
@InheritConstructors
class DefaultLayout extends VelocityLayout {

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

        def titleBar = regions.title.bounds
        if (card.castingCost != null) {
            def cost = tool.costAsIcons

            cost.unique(false).each iconDef.curry("cost")
            def gc = el(doc.rootElement, 'g', [
                id: "casting-cost"
            ])
            float x = 0
            cost.each {
                def factor = (float) titleBar.height / it.size.height
                def width = it.size.width * factor
                el(gc, 'use', [
                    'xlink:href': "#cost-$it.key",
                    transform: "translate($x 0) scale($factor)"
                ])
                x += width
            }
            elattr(gc, [
                transform: "translate(${titleBar.x + titleBar.width - x} $titleBar.y)"
            ])
            // constrain the title bar to what's left after the cost plus half the average icon width
            def avgCostIconWidth = x / cost.size()
            titleBar -= new DoubleDimension(x + avgCostIconWidth / 2, 0)
        }
        g.color = tool.barTexture.textColor
        layoutUtils.line(titleBar, card.title, regions.title.textAttributes).draw(g)
        xmlImage(g, regions.artwork, card.artwork)

        def typeBar = regions.type.bounds
        if (card.rarity && card.setKey) {
            def icon = tool.setIcon
            iconDef("set", icon)
            def availHeight = typeBar.height * 1.3
            def factor = (float) availHeight / icon.size.height
            def width = icon.size.width * factor
            el(doc.rootElement, 'use', [
                'xlink:href': "#set-$icon.key",
                transform: "translate(${typeBar.x + typeBar.width - width}, ${typeBar.y - (availHeight - typeBar.height) / 1.8}) scale($factor)"
            ])
            typeBar -= new DoubleDimension(width * 1.4, 0)
        }
        layoutUtils.line(typeBar, card.typeParts.join(" ") + (card.subtypeParts ? ' \u2014 ' + card.subtypeParts.join(" ") : ""), regions.type.textAttributes).draw(g)

        tool.bodyIcons.each iconDef.curry("body")
        g.color = tool.textboxTextures.first().textColor
        layoutUtils.block(g, regions.textbox.bounds, tool.bodyText, regions.textbox.font, regions.textbox.italicFont, { SVGGraphics2D gphcs, Rectangle2D box, Icon it ->
            el(doc.rootElement, 'use', [
                'xlink:href': "#body-$it.key",
                transform: "translate($box.x $box.y)" + (box.size == it.size ? '' : " scale(${(float) box.width / it.size.width} ${(float) box.height / it.size.height})")
            ])
        })

        if (card instanceof CreatureCard) {
            g.color = tool.barTexture.textColor
            layoutUtils.line(regions.powerToughness.bounds, "$card.power/$card.toughness", regions.powerToughness.textAttributes, Align.CENTER).draw(g)
        }
        g.color = tool.frameTextures.first().textColor
        layoutUtils.line(regions.artist.bounds, card.artwork.artist, regions.artist.textAttributes).draw(g)
        layoutUtils.line(regions.footer.bounds, "$card.copyright $card.cardNumber/$card.setCardCount", regions.footer.textAttributes).draw(g)

//        g.color = Color.YELLOW
//        g.draw(TITLE_BAR)
//        g.draw(titleBar)
//        g.draw(ARTWORK)
//        g.draw(TYPE_BAR)
//        g.draw(TEXTBOX)
//        g.draw(POWER_TOUGHNESS)
//        g.draw(ARTIST)
//        g.draw(FOOTER)

        doc.documentElement.appendChild(g.topLevelGroup)
    }

    protected void xmlImage(SVGGraphics2D svgg, Rectangle2D box, RasterImage ri) {
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
