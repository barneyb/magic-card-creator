package com.barneyb.magic.creator.icon
import com.barneyb.magic.creator.api.CardSetIconFactory
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.core.DefaultIcon
import com.barneyb.magic.creator.util.Align
import com.barneyb.magic.creator.util.ColorUtils
import com.barneyb.magic.creator.util.FontLoader
import com.barneyb.magic.creator.util.SvgUtils
import com.barneyb.magic.creator.util.TextLayoutUtils
import com.barneyb.magic.creator.util.XmlUtils

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.font.TextAttribute
import java.awt.geom.AffineTransform
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
    com.barneyb.magic.creator.api.Icon getIcon(String key) {
        getIcon(key, Rarity.COMMON)
    }

    @Override
    com.barneyb.magic.creator.api.Icon getIcon(String key, Rarity rarity) {
        def iconKey = (key + "-" + rarity.name()).toLowerCase()
        if (! icons.containsKey(iconKey)) {
            def colors = {
                switch (rarity) {
                    case Rarity.COMMON:
                        return [Color.WHITE, Color.BLACK]
                    case Rarity.UNCOMMON:
                        return [Color.BLACK, new Color(192, 192, 192)]
                    case Rarity.RARE:
                        return [Color.BLACK, new Color(255, 237, 136)]
                    case Rarity.MYTHIC_RARE:
                        return [Color.BLACK, new Color(255, 165, 52)]
                }
            }()
            def id = "set-" + Math.random().toString().substring(2)
            def doc = XmlUtils.read("""\
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="80" height="45">
    <defs>
        <path id="$id" d="
            M 0 -20
            h 32
            c 7 10 7 30 0 40
            h -64
            c -7 -10 -7 -30 0 -40
            z
        " />
    </defs>
    <g transform="translate(40 22.5)">
        <use xlink:href="#$id" stroke="${ColorUtils.toHex(colors[1])}" stroke-width="5" fill="none" />
        <g transform="scale(0.98 0.99)">
            <use xlink:href="#$id" stroke-width="0" fill="${ColorUtils.toHex(colors[0])}" />
        </g>
        <g transform="scale(0.94 0.90)">
            <use xlink:href="#$id" stroke-width="0" fill="${ColorUtils.toHex(colors[1])}" />
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
                TextLayoutUtils.line(new Rectangle(11, 3, 58, 27), key, attrs, Align.STRETCH).draw(g)
            }
            icons[iconKey] = new DefaultIcon(iconKey, doc)
        }
        return icons[iconKey]
    }

}
