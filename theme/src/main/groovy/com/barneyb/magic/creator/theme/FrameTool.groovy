package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.IconGroup
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.api.Texture
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.core.DefaultIconGroup
import com.barneyb.magic.creator.util.ColorUtils
import groovy.transform.Memoized
import groovy.transform.TupleConstructor

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class FrameTool {

    Theme theme
    Card card

    String unique() {
        Math.random().toString().substring(2)
    }

    boolean isLand() {
        card.isType('land')
    }

    boolean isCreature() {
        card.isType('creature')
    }

    boolean isArtifact() {
        card.isType('artifact')
    }

    boolean isSemiEnchantment() {
        card.semiEnchantment
    }

    Texture getSemiEnchantmentTexture() {
        theme.semiEnchantmentTexture
    }

    Texture getBarTexture() {
        def tc
        if (land || card.hybrid) {
            tc = ThemedColor.LAND
        } else if (artifact) {
            tc = ThemedColor.ARTIFACT
        } else if (card.colors.size() == 1) {
            tc = color(card.colors.first())
        } else {
            tc = ThemedColor.GOLD
        }
        theme.getColorTheme(tc).barTexture
    }

    ThemedColor color(ManaColor c) {
        switch (c) {
            case ManaColor.COLORLESS:
                return artifact ? ThemedColor.ARTIFACT : ThemedColor.LAND
            case ManaColor.WHITE:
                return ThemedColor.WHITE
            case ManaColor.BLUE:
                return ThemedColor.BLUE
            case ManaColor.BLACK:
                return ThemedColor.BLACK
            case ManaColor.RED:
                return ThemedColor.RED
            case ManaColor.GREEN:
                return ThemedColor.GREEN
        }
    }

    String toValue(Color c) {
        ColorUtils.toHex(c)
    }

    String toDataUrl(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        ImageIO.write(image, "PNG", out)
        byte[] bytes = out.toByteArray()
        String img = "data:image/png;base64," + new String(Base64.encoder.encode(bytes))
        img
    }

    float negate(Number n) {
        n == 0 ? n : (float) -1 * n
    }

    List<Texture> getFrameTextures() {
        def tcs
        if (land) {
            tcs = [ThemedColor.LAND]
        } else if (artifact) {
            tcs = [ThemedColor.ARTIFACT]
        } else if (card.multiColor && (! card.hybrid || card.colors.size() > 2)) {
            tcs = [ThemedColor.GOLD]
        } else {
            tcs = card.colors.collect this.&color
        }
        tcs.collect {
            theme.getColorTheme(it).frameTexture
        }
    }

    List<Texture> getTextboxTextures() {
        def tcs
        if (land) {
            if (card.alliedColors != null && card.alliedColors.size() in 1..2) {
                return card.alliedColors.collect {
                    def ct = theme.getColorTheme(color(it))
                    ct.manaTextboxTexture ?: ct.textboxTexture
                }
            } else {
                tcs = [ThemedColor.LAND]
            }
        } else if (card.colors.contains(ManaColor.COLORLESS) && card.colors.size() in 2..3) {
            tcs = (card.colors - ManaColor.COLORLESS).collect this.&color
        } else if (card.colors.size() in 1..2) {
            tcs = card.colors.collect this.&color
        } else if (card.multiColor) {
            tcs = [ThemedColor.GOLD]
        } else if (artifact) {
            tcs = [ThemedColor.ARTIFACT]
        } else {
            tcs = card.colors.collect this.&color
        }
        tcs.collect {
            theme.getColorTheme(it).textboxTexture
        }
    }

    List<Color> getBorderColors() {
        def tcs
        if (land) {
            if (card.alliedColors != null && card.alliedColors.size() in 1..2) {
                tcs = card.alliedColors.collect this.&color
            } else {
                tcs = [ThemedColor.LAND]
            }
        } else if (card.colors.contains(ManaColor.COLORLESS) && card.colors.size() in 2..3) {
            tcs = (card.colors - ManaColor.COLORLESS).collect this.&color
        } else if (card.colors.size() in 1..2) {
            tcs = card.colors.collect this.&color
        } else if (card.multiColor) {
            tcs = [ThemedColor.GOLD]
        } else if (artifact) {
            tcs = [ThemedColor.ARTIFACT]
        } else {
            tcs = card.colors.collect this.&color
        }
        tcs.collect {
            theme.getColorTheme(it).borderColor
        }
    }

    Color getPowerToughnessBoxColor() {
        borderColors.last()
    }

    float gradientStart(int index, int count) {
        float step = 1.0 / count
        float point = step * index
        point - step / 5
    }

    float gradientEnd(int index, int count) {
        float step = 1.0 / count
        float point = step * index
        point + step / 5
    }

    float gradientPoint(int index, int count) {
        float step = 1.0 / (count + 1)
        step * (index + 1)
    }

    IconGroup getCostAsIcons() {
        new DefaultIconGroup(card.castingCost?.collect {
            theme.symbolIconFactory.getShadowedIcon(it)
        })
    }

    Set<Icon> getBodyIcons() {
        def icons = [] as Set
        def doIt
        doIt = {
            if (it instanceof Icon) {
                icons << it
            } else if (it instanceof Collection) {
                it.each doIt
            }
        }
        bodyText.each doIt
        icons
    }

    @Memoized
    List<List<BodyItem>> getBodyText() {
        def r = []
        if (card.rulesText != null) {
            r.addAll(symbolsToIcons(card.rulesText))
        }
        if (card.flavorText != null) {
            r.addAll(symbolsToIcons(card.flavorText))
        }
        r
    }

    List<List<BodyItem>> symbolsToIcons(List<List<BodyItem>> lines) {
        lines.collect { line ->
            line.collect {
                if (it instanceof Symbol) {
                    theme.symbolIconFactory.getIcon(it)
                } else if (it instanceof SymbolGroup) {
                    theme.symbolIconFactory.getIcons(it)
                } else {
                    it
                }
            }
        }
    }

    Icon getSetIcon() {
        theme.cardSetIconFactory.getIcon(card.set, card.rarity)
    }

}
