package com.barneyb.magic.creator.descriptor
import org.junit.Test

import static com.barneyb.magic.creator.descriptor.FrameBaseType.*
import static com.barneyb.magic.creator.descriptor.FrameModifier.*
import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class CompoundFrameTest {

    @Test
    void keys() {
        assertEquals("artifact_creature", (ARTIFACT + Type.CREATURE).assetKey)
        assertEquals("gold_enchantment_creature_blue_black", (GOLD + Type.ENCHANTMENT_CREATURE + Dual.BLUE_BLACK).assetKey)
    }

    @Test
    void plus() {
        assertEquals(new CompoundFrame(ARTIFACT, [Type.CREATURE, Dual.BLUE_BLACK]), ARTIFACT + Type.CREATURE + Dual.BLUE_BLACK)
        assertEquals(new CompoundFrame(ARTIFACT, [Type.CREATURE]), ARTIFACT + Type.CREATURE + Type.CREATURE)
    }

    @Test(expected = IllegalArgumentException)
    void multipleModsOfSameType() {
        assertEquals(new CompoundFrame(ARTIFACT, [Type.CREATURE]), ARTIFACT + Type.CREATURE + Type.ENCHANTMENT_CREATURE)
    }

    @Test
    void minus() {
        assertEquals(ARTIFACT, ARTIFACT + Type.CREATURE - Type.CREATURE)
        assertEquals(new CompoundFrame(ARTIFACT, [Dual.BLUE_BLACK]), ARTIFACT + Dual.BLUE_BLACK - Type.CREATURE)
    }

}
