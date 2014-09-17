package com.barneyb.magic.creator.icon

import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CardSetIconFactory
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.core.DefaultIcon
import com.barneyb.magic.creator.util.Align
import com.barneyb.magic.creator.util.DoubleDimension
import com.barneyb.magic.creator.util.FontLoader
import com.barneyb.magic.creator.util.SvgUtils
import com.barneyb.magic.creator.util.TextLayoutUtils
import com.barneyb.magic.creator.util.XmlUtils
import groovy.util.logging.Log
import org.w3c.dom.svg.SVGDocument

import java.awt.Color
import java.awt.Graphics2D
import java.awt.font.TextAttribute
import java.awt.geom.AffineTransform
import java.awt.geom.Dimension2D
import java.awt.geom.Rectangle2D

import static com.barneyb.magic.creator.util.ColorUtils.*

/**
 *
 *
 * @author barneyb
 */
@Log
class DefaultCardSetIconFactory implements CardSetIconFactory {

    static final String THE_PATH = """\
        <path id="path" d="
            M 0 -20
            h 25
            c 6 10 6 30 0 40
            h -50
            c -6 -10 -6 -30 0 -40
            z
        " />"""

    protected Map<String, com.barneyb.magic.creator.api.Icon> icons = [:]

    static {
        FontLoader.fromClasspath(
            "fonts/GoudyOldStyle-Regular.ttf"
        )
    }

    @Override
    com.barneyb.magic.creator.api.Icon getIcon(CardSet cs) {
        getIcon(cs, Rarity.COMMON)
    }

    @Override
    com.barneyb.magic.creator.api.Icon getIcon(CardSet cs, Rarity rarity) {
        def setKey = cs?.key ?: "??"
        def iconKey = (setKey + "-" + rarity.name()).toLowerCase()
        if (! icons.containsKey(iconKey)) {
            icons[iconKey] = new DefaultIcon(
                iconKey,
                cs?.iconSymbol == null
                    ? compose(getDefaultField(), getDefaultSymbol(setKey), rarity)
                    : compose(cs.iconField.document, cs.iconSymbol.document, rarity)
            )
        }
        icons[iconKey]
    }

    protected SVGDocument getDefaultField() {
        XmlUtils.read("""\
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="60" height="40">
    <g transform="translate(30 20)">
        $THE_PATH
    </g>
</svg>""")
    }

    protected SVGDocument getDefaultSymbol(String key) {
        def doc = XmlUtils.read("""\
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="60" height="40">
    <defs>
        $THE_PATH
        <mask id="mask">
            <use xlink:href="#path" fill="#fff" />
            <g transform="scale(0.9 0.84)">
                <use xlink:href="#path" fill="#000" />
            </g>
        </mask>
    </defs>
    <g transform="translate(30 20)">
        <g transform="scale(0.94 0.9)">
            <use xlink:href="#path" mask="url(#mask)" />
        </g>
    </g>
</svg>""")
        SvgUtils.withGraphics(doc) { Graphics2D g ->
            def attrs = [
                (TextAttribute.FAMILY): "Goudy Old Style",
                (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
            ]
            def fontSize = TextLayoutUtils.fontSizeForHeight(27, key, attrs, false)
            attrs[TextAttribute.SIZE] = fontSize
            g.transform = AffineTransform.getScaleInstance(1, 1.3)
            TextLayoutUtils.line(new Rectangle2D.Float(8, 1.5f, 44, 27), key, attrs, Align.STRETCH).draw(g)
        }
        doc
    }

    protected SVGDocument compose(SVGDocument field, SVGDocument symbol, Rarity rarity) {
        Dimension2D size
        if (field == null) {
            field = symbol
            symbol = null
            size = SvgUtils.size(field)
        } else {
            def fs = SvgUtils.size(field)
            def ss = SvgUtils.size(symbol)
            if (fs == ss) {
                size = fs
            } else {
                log.warning("Set icon field and symbol are different sizes: $fs vs. $ss")
                size = new DoubleDimension(Math.max(fs.width, ss.width), Math.max(fs.height, ss.height))
            }
        }
        def colors = {
            switch (rarity) {
                case Rarity.COMMON:
                    return [Color.WHITE, Color.BLACK, Color.BLACK]
                case Rarity.UNCOMMON:
                    return [Color.BLACK, fromHex("#54707E"), fromHex("#C8E2EF")]
                case Rarity.RARE:
                    return [Color.BLACK, fromHex("#8C723C"), fromHex("#E5C77F")]
                case Rarity.MYTHIC_RARE:
                    return [Color.BLACK, fromHex("#B02B24"), fromHex("#F7971B")]
            }
        }()
        def id = "field-" + Math.random().toString().substring(2)
        def doc = XmlUtils.read("""\
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="$size.width" height="$size.height">
    <defs>
        <linearGradient id="$id-fill" gradientTransform="translate(-${size.width / 2} 0) rotate(-30)" gradientUnits="userSpaceOnUse">
            <stop offset="0" stop-color="${toHex(colors[1])}" />
            <stop offset="0.5" stop-color="${toHex(colors[2])}" />
            <stop offset="1" stop-color="${toHex(colors[1])}" />
        </linearGradient>
    </defs>
</svg>
""")
        def add = { sub, fill ->
            def node = sub.rootElement.cloneNode(true)
            doc.adoptNode(node)
            XmlUtils.el(doc.rootElement, 'g', [
                fill: fill
            ]).appendChild(node)
        }
        add(field, "url(#$id-fill)")
        if (symbol) {
            add(symbol, toHex(colors[0]))
        }

        doc
    }

    protected com.barneyb.magic.creator.api.Icon getIcon(String key, Rarity rarity) {
        new DefaultIcon(key, compose(getDefaultField(), getDefaultSymbol(key), rarity))
    }

}
