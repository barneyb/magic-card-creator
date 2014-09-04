package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Texture
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.util.ColorUtils
import groovy.transform.TupleConstructor

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class FrameTool {

    DefaultTheme theme
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
        if (land) {
            tc = ThemedColor.LAND
        } else if (artifact) {
            tc = ThemedColor.ARTIFACT
        } else if (card.colors.size() == 1) {
            tc = color card.colors.first()
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

    float invert(Number n) {
        (float) -1 * n
    }

}
