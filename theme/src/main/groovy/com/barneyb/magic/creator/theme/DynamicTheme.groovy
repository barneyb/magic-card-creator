package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSetIconFactory
import com.barneyb.magic.creator.api.ColorTheme
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.api.Texture
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.core.ServiceUtils
import com.barneyb.magic.creator.core.SimpleColorTheme
import com.barneyb.magic.creator.core.SimpleTexture
import com.barneyb.magic.creator.core.UrlRasterImage
import com.barneyb.magic.creator.icon.DefaultCardSetIconFactory
import com.barneyb.magic.creator.icon.DefaultSymbolIconFactory
import com.barneyb.magic.creator.util.FontLoader
import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import org.w3c.dom.svg.SVGDocument
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@Log
class DynamicTheme implements Theme {

    SymbolIconFactory symbolIconFactory = new DefaultSymbolIconFactory()
    CardSetIconFactory cardSetIconFactory = new DefaultCardSetIconFactory()

    protected ThemeSpec desc
    protected Map<LayoutType, VelocityLayout> layouts
    protected Map<ThemedColor, ColorTheme> colors
    Texture semiEnchantmentTexture

    def DynamicTheme(ThemeSpec desc) {
        this.desc = desc
        if (desc.icons) {
            def sif = ServiceUtils.load(SymbolIconFactory, desc.icons)
            if (sif == null) {
                log.warning("No '$desc.icons' icon factory is known, using default")
            } else {
                symbolIconFactory = sif
            }
        }
        semiEnchantmentTexture = textureFromSpec(desc.semiEnchantment)
        layouts = [:]
        desc.layouts.each { t, k ->
            def v = desc.library.layouts[k]
            def layout = v.impl.newInstance()
            layout.theme = this
            layout.template = v.template.toString()
            layout.regions = v.regions
            layouts[t] = layout
        }
        if (desc.library?.fonts) {
            FontLoader.fromUrl(desc.library.fonts)
        }
        colors = [:]
        desc.colors.each { k, v ->
            colors[k] = colorFromSpec(v)
        }
    }

    protected ColorTheme colorFromSpec(ColorThemeSpec spec) {
        new SimpleColorTheme(
            spec.border,
            textureFromSpec(spec.frame),
            textureFromSpec(spec.bar),
            textureFromSpec(spec.textbox),
            textureFromSpec(spec.mana)
        )
    }

    protected SimpleTexture textureFromName(String name) {
        if (name != null) {
            def spec = desc.library?.textures?.get(name)
            if (spec != null) {
                if (spec.base == name) {
                    throw new IllegalStateException("Texture '$name' is based on itself!?")
                }
                def tex = textureFromSpec(spec)
                if (tex != null) {
                    return tex
                }
            }
        }
        throw new IllegalArgumentException("No '$name' texture is known.")
    }

    protected SimpleTexture textureFromSpec(TextureSpec spec) {
        if (spec == null) {
            null
        } else {
            def texture = new SimpleTexture(
                spec.image == null ? null : new UrlRasterImage(spec.image),
                spec.opacity,
                spec.over,
                spec.under,
                spec.bounds,
                spec.text
            )
            if (spec.base != null) {
                texture = textureFromName(spec.base) + texture
            }
            texture
        }
    }

    @Override
    String getName() {
        desc.name
    }

    @Override
    boolean supports(LayoutType type) {
        layouts.containsKey(type)
    }

    @Override
    ColorTheme getColorTheme(ThemedColor color) {
        if (! colors.containsKey(color)) {
            throw new IllegalArgumentException("This theme does not support the $color color.")
        }
        colors[color]
    }


    @Override
    SVGDocument layout(Card card) {
        if (! supports(card.layoutType)) {
            throw new IllegalArgumentException("This theme does not support the $card.layoutType layout type.")
        }
        layouts[card.layoutType].layout(card)
    }

}
