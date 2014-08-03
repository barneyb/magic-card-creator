package com.barneyb.magic.creator.asset

import com.barneyb.magic.creator.descriptor.CompoundFrame
import com.barneyb.magic.creator.descriptor.FrameBaseType
import com.barneyb.magic.creator.descriptor.FrameModifier
import org.junit.Before
import org.junit.Test

import java.awt.*

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class FrameAssetSetTest {

    FrameAssetSet fas

    protected ClasspathImage load(String path) {
        new ClasspathImage("m", "assets/screen/frames/" + path, new Dimension(20, 20))
    }

    @Before
    void makeFas() {
        fas = new FrameAssetSet(
            renderSet: new RenderSet(
                key: "screen"
            ),
            key: 'frames',
            type: 'png'
        )
    }

    @Test
    void stripDual() {
        assertEquals(
            load("gold.png"),
            fas.getImageAsset(new CompoundFrame(FrameBaseType.GOLD, [FrameModifier.Dual.BLACK_GREEN]))
        )
    }

    @Test
    void stripEnchantmentCreature() {
        assertEquals(
            load("red_creature.png"),
            fas.getImageAsset(new CompoundFrame(FrameBaseType.RED, [FrameModifier.Animated.CREATURE, FrameModifier.Enchanted.ENCHANTMENT]))
        )
    }

    @Test
    void stripJustDual() {
        assertEquals(
            load("red_creature.png"),
            fas.getImageAsset(new CompoundFrame(FrameBaseType.RED, [FrameModifier.Animated.CREATURE, FrameModifier.Dual.BLACK_GREEN]))
        )
    }

}
