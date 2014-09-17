package com.barneyb.magic.creator.icon

import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CardSetIconFactory
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.core.DefaultIcon
import com.barneyb.magic.creator.util.Align
import com.barneyb.magic.creator.util.FontLoader
import com.barneyb.magic.creator.util.SvgUtils
import com.barneyb.magic.creator.util.TextLayoutUtils
import com.barneyb.magic.creator.util.XmlUtils

import java.awt.Color
import java.awt.Graphics2D
import java.awt.font.TextAttribute
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D

import static com.barneyb.magic.creator.util.ColorUtils.*
/**
 *
 *
 * @author barneyb
 */
class DefaultCardSetIconFactory implements CardSetIconFactory {

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
        getIcon(cs.key, rarity)
    }

    protected com.barneyb.magic.creator.api.Icon getIcon(String key, Rarity rarity) {
        def iconKey = (key + "-" + rarity.name()).toLowerCase()
        if (! icons.containsKey(iconKey)) {
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
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="60" height="40">
    <defs>
        <path id="$id" d="
            M 0 -20
            h 25
            c 6 10 6 30 0 40
            h -50
            c -6 -10 -6 -30 0 -40
            z
        " />
        <linearGradient id="$id-fill" gradientTransform="translate(-30 0) rotate(-30)" gradientUnits="userSpaceOnUse">
            <stop offset="0" stop-color="${toHex(colors[1])}" />
            <stop offset="0.5" stop-color="${toHex(colors[2])}" />
            <stop offset="1" stop-color="${toHex(colors[1])}" />
        </linearGradient>
    </defs>
    <g transform="translate(30 20)">
        <use xlink:href="#$id" stroke-width="0" fill="url(#$id-fill)" />
        <g transform="scale(0.9 0.84)">
            <use xlink:href="#$id" stroke-width="3" stroke="${toHex(colors[0])}" fill="none" />
        </g>
    </g>
</svg>
""")

            SvgUtils.withGraphics(doc) { Graphics2D g ->
                def attrs = [
                    (TextAttribute.FAMILY): "Goudy Old Style",
                    (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
                ]
                def fontSize = TextLayoutUtils.fontSizeForHeight(27, key, attrs, false)
                attrs[TextAttribute.SIZE] = fontSize
                g.color = colors[0]
                g.transform = AffineTransform.getScaleInstance(1, 1.3)
                TextLayoutUtils.line(new Rectangle2D.Float(8, 1.5f, 44, 27), key, attrs, Align.STRETCH).draw(g)
            }
            icons[iconKey] = new DefaultIcon(iconKey, doc)
        }
        return icons[iconKey]
    }

}
