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

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
class DefaultTheme implements Theme {

    protected SymbolIconFactory iconFactory = new DefaultIconFactory()

//    <pattern id="enchantmentFrame" patternUnits="userSpaceOnUse" height="480" width="640">
//        <image width="640" height="480" xlink:href="starfield.jpg" opacity=".6" />
//    </pattern>
    protected Texture semiEnchantmentTexture = new SimpleTexture(
        image: new ClasspathRasterImage("theme/default/starfield.jpg"),
        opacity: 0.6f
    )


//    <pattern id="bar" patternUnits="userSpaceOnUse" height="240" width="240">
//        <image x="-10" y="-10" width="256" height="256" xlink:href="metal.jpg" />
//        <rect width="240" height="240" fill="white" opacity=".3" />
//    </pattern>
    protected Texture barTexture = new SimpleTexture(
        image: new ClasspathRasterImage("theme/default/metal.jpg"),
        overFlood: new SimpleFlood(Color.WHITE, 0.3f),
        bounds: new Rectangle(10, 10, 240, 240)
    )

    protected Map<ThemedColor, ? extends ColorTheme> colors = [
        (ThemedColor.WHITE): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#ffffff"),
//            <pattern id="whiteFrame" patternUnits="userSpaceOnUse" height="600" width="600">
//                <image width="600" height="600" xlink:href="white.jpg" />
//            </pattern>
            frameTexture: new SimpleTexture(new ClasspathRasterImage("theme/default/white.jpg")),
            barTexture: barTexture,
//            <pattern id="whiteTextbox" patternUnits="userSpaceOnUse" height="480" width="640">
//                <image width="640" height="480" xlink:href="cloud.jpg" />
//                <rect width="640" height="480" fill="#fffafc" opacity=".8" />
//            </pattern>
            textboxTexture: new SimpleTexture(
                image: new ClasspathRasterImage("theme/default/cloud.jpg"),
                overFlood: new SimpleFlood(ColorUtils.fromHex("#fffafc"), 0.8f)
            ),
        ),
        (ThemedColor.RED): new SimpleColorTheme(
            baseColor: ColorUtils.fromHex("#ff0000"),
//            <pattern id="redFrame" patternUnits="userSpaceOnUse" height="400" width="640">
//                <image width="640" height="400" xlink:href="red.jpg" />
//            </pattern>
            frameTexture: new SimpleTexture(new ClasspathRasterImage("theme/default/red.jpg")),
            barTexture: barTexture,
//            <pattern id="redTextbox" patternUnits="userSpaceOnUse" height="400" width="640">
//                <image width="640" height="400" xlink:href="red.jpg" />
//                <rect width="640" height="400" fill="#fee" opacity=".8" />
//            </pattern>
            textboxTexture: new SimpleTexture(
                image: new ClasspathRasterImage("theme/default/red.jpg"),
                overFlood: new SimpleFlood(ColorUtils.fromHex("#ffeeee"), 0.8f)
            ),
        ),
    ]

    @Override
    String getName() {
        "Default M14-ish Theme"
    }

    protected ColorTheme getColorTheme(ThemedColor color) {
        if (! colors.containsKey(color)) {
            throw new IllegalArgumentException("This theme does not support the $color color.")
        }
        colors[color]
    }

    @Override
    boolean supports(LayoutType type) {
        type in [
            LayoutType.SPELL,
            LayoutType.CREATURE
        ]
    }

    @Override
    SVGDocument layout(Card card) {
        new DefaultLayout().layout(card)
    }

}
