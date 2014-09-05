package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.api.Texture
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.util.ColorUtils
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

    boolean isLand() {
        card.layoutType == LayoutType.LAND
    }

    boolean isCreature() {
        card.layoutType == LayoutType.CREATURE
    }

    boolean isArtifact() {
        card.typeParts*.toLowerCase().contains('artifact')
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
                return ThemedColor.LAND
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
            theme.getColorTheme(it).baseColor
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

    List<Icon> getCostAsIcons() {
        card.castingCost?.collect {
            theme.symbolIconFactory.getShadowedIcon(it)
        }
    }

    List<List<BodyItem>> getBodyAsIcons() {
        bodySymbolToIcon(card.rulesText)
    }

    List<List<BodyItem>> getFlavorAsIcons() {
        bodySymbolToIcon(card.rulesText)
    }

    protected List<List<BodyItem>> bodySymbolToIcon(List<List<BodyItem>> body) {
        body?.each { line ->
            line.eachWithIndex { BodyItem it, int i ->
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

}
