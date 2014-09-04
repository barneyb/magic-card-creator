package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.ColorTheme
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.api.Texture
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.core.ClasspathRasterImage
import com.barneyb.magic.creator.core.SimpleColorTheme
import com.barneyb.magic.creator.core.SimpleFlood
import com.barneyb.magic.creator.core.SimpleTexture
import com.barneyb.magic.creator.icon.DefaultIconFactory
import com.barneyb.magic.creator.util.ColorUtils
import org.w3c.dom.svg.SVGDocument

import java.awt.Color
import java.awt.Rectangle

/**
 *
 *
 * @author barneyb
 */
class DefaultTheme implements Theme {

    protected SymbolIconFactory iconFactory = new DefaultIconFactory()

    Texture semiEnchantmentTexture = new SimpleTexture(
        image: new ClasspathRasterImage("theme/default/starfield.jpg"),
        opacity: 0.6f
    )

    protected SimpleTexture barTexture = new SimpleTexture(
        image: new ClasspathRasterImage("theme/default/metal.jpg"),
        overFlood: new SimpleFlood(Color.WHITE, 0.3f),
        bounds: new Rectangle(10, 10, 240, 240)
    )

    protected Map<ThemedColor, ? extends ColorTheme> colors = [
        (ThemedColor.WHITE): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#ffffff"),
            frameTexture: new SimpleTexture(new ClasspathRasterImage("theme/default/white.jpg")),
            barTexture: barTexture,
            textboxTexture: new SimpleTexture(
                image: new ClasspathRasterImage("theme/default/cloud.jpg"),
                overFlood: new SimpleFlood(ColorUtils.fromHex("#fffafc"), 0.8f)
            ),
        ),
        (ThemedColor.BLUE): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#9FC5FF"),
            frameTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#D9E8FF"), 0.6f)),
            barTexture: barTexture,
            textboxTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#D9E8FF"), 0.8f)),
        ),
        (ThemedColor.BLACK): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#666666"),
            frameTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#A6A6A6"), 0.6f)),
            barTexture: barTexture,
            textboxTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#A6A6A6"), 0.8f)),
        ),
        (ThemedColor.RED): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#ff0000"),
            frameTexture: new SimpleTexture(new ClasspathRasterImage("theme/default/red.jpg")),
            barTexture: barTexture,
            textboxTexture: new SimpleTexture(
                image: new ClasspathRasterImage("theme/default/red.jpg"),
                overFlood: new SimpleFlood(ColorUtils.fromHex("#ffeeee"), 0.8f)
            ),
        ),
        (ThemedColor.GREEN): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#A2D192"),
            frameTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#BDD1B6"), 0.6f)),
            barTexture: barTexture,
            textboxTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#BDD1B6"), 0.8f)),
        ),
        (ThemedColor.GOLD): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#E8E78D"),
            frameTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#E8E8B7"), 0.6f)),
            barTexture: barTexture,
            textboxTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#E8E8B7"), 0.8f)),
        ),
        (ThemedColor.ARTIFACT): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#AAD7EC"),
            frameTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#C4DFEC"), 0.6f)),
            barTexture: barTexture,
            textboxTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#C4DFEC"), 0.8f)),
        ),
        (ThemedColor.LAND): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#FFD798"),
            frameTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#FFEED4"), 0.6f)),
            barTexture: barTexture,
            textboxTexture: barTexture.floodOver(new SimpleFlood(ColorUtils.fromHex("#FFEED4"), 0.8f)),
        ),
    ]

    @Override
    String getName() {
        "Default M14-ish Theme"
    }

    ColorTheme getColorTheme(ThemedColor color) {
        if (! colors.containsKey(color)) {
            throw new IllegalArgumentException("This theme does not support the $color color.")
        }
        colors[color]
    }

    @Override
    boolean supports(LayoutType type) {
        type in [
            LayoutType.LAND,
            LayoutType.SPELL,
            LayoutType.CREATURE
        ]
    }

    @Override
    SVGDocument layout(Card card) {
        if (! supports(card.layoutType)) {
            throw new IllegalArgumentException("This theme does not support the $card.layoutType layout type.")
        }
        new VelocityLayout(this, "theme/default/frame.svg.vm").layout(card)
    }

}
