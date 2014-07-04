package com.barneyb.magic.creator.asset

import com.barneyb.magic.creator.descriptor.CompoundFrame
import com.barneyb.magic.creator.descriptor.FrameBaseType
import com.barneyb.magic.creator.descriptor.FrameModifier
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class FrameAssetSetTest {

    FrameAssetSet fas
    
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
            new ClasspathImage("assets/screen/frames/gold.png"),
            fas.getImageAsset(new CompoundFrame(FrameBaseType.GOLD, [FrameModifier.Dual.BLACK_GREEN]))
        )
    }

    @Test
    void stripEnchantmentCreature() {
        assertEquals(
            new ClasspathImage("assets/screen/frames/red_creature.png"),
            fas.getImageAsset(new CompoundFrame(FrameBaseType.RED, [FrameModifier.Type.ENCHANTMENT_CREATURE]))
        )
    }

    @Test
    void stripJustDual() {
        assertEquals(
            new ClasspathImage("assets/screen/frames/red_creature.png"),
            fas.getImageAsset(new CompoundFrame(FrameBaseType.RED, [FrameModifier.Type.CREATURE, FrameModifier.Dual.BLACK_GREEN]))
        )
    }

}
