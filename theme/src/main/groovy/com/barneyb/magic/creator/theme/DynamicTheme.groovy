package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.ColorTheme
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.api.Texture
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.core.SimpleColorTheme
import com.barneyb.magic.creator.core.SimpleTexture
import com.barneyb.magic.creator.core.UrlRasterImage
import com.barneyb.magic.creator.icon.DefaultIconFactory
import groovy.transform.TupleConstructor
import org.w3c.dom.svg.SVGDocument
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DynamicTheme implements Theme {

    SymbolIconFactory symbolIconFactory = new DefaultIconFactory()

    protected ThemeSpec desc
    protected Map<LayoutType, VelocityLayout> layouts
    protected Map<ThemedColor, ColorTheme> colors
    Texture semiEnchantmentTexture

    def DynamicTheme(ThemeSpec desc) {
        this.desc = desc
        semiEnchantmentTexture = textureFromSpec(desc.semiEnchantment)
        //noinspection GroovyAssignabilityCheck
        layouts = desc.layouts.collectEntries { k, LayoutSpec v ->
            def layout = v.impl.newInstance()
            layout.theme = this
            layout.template = v.template.toString()
            new MapEntry(
                LayoutType.valueOf(k.toUpperCase()),
                layout
            )
        }
        //noinspection GroovyAssignabilityCheck
        colors = desc.colors.collectEntries { k, v ->
            new MapEntry(ThemedColor.valueOf(k.toUpperCase()), colorFromSpec(v))
        }
    }

    protected ColorTheme colorFromSpec(ColorThemeSpec spec) {
        new SimpleColorTheme(
            spec.raw,
            textureFromSpec(spec.frame),
            textureFromSpec(spec.bar),
            textureFromSpec(spec.textbox),
            textureFromSpec(spec.mana)
        )
    }

    protected SimpleTexture textureFromName(String name) {
        if (name != null) {
            def spec = desc.textures[name]
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
                spec.bounds
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
