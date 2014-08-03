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
        assertEquals("artifact_creature", (ARTIFACT + Animated.CREATURE).assetKey)
        assertEquals("gold_enchantment_creature_blue_black", (GOLD + Animated.CREATURE + Enchanted.ENCHANTMENT + Dual.BLUE_BLACK).assetKey)
    }

    @Test
    void plus() {
        assertEquals(new CompoundFrame(ARTIFACT, [Animated.CREATURE, Dual.BLUE_BLACK]), ARTIFACT + Animated.CREATURE + Dual.BLUE_BLACK)
        assertEquals(new CompoundFrame(ARTIFACT, [Animated.CREATURE]), ARTIFACT + Animated.CREATURE + Animated.CREATURE)
    }

    @Test(expected = IllegalArgumentException)
    void multipleModsOfSameType() {
        ARTIFACT + Dual.BLACK_GREEN + Dual.BLUE_RED
    }

    @Test
    void minus() {
        assertEquals(ARTIFACT, ARTIFACT + Animated.CREATURE - Animated.CREATURE)
        assertEquals(new CompoundFrame(ARTIFACT, [Dual.BLUE_BLACK]), ARTIFACT + Dual.BLUE_BLACK - Animated.CREATURE)
    }

}
