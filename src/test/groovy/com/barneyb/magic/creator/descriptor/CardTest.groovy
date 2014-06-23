package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static org.junit.Assert.*

/**
 *
 * @author bboisvert
 */
class CardTest {

    @Test
    void getCost() {
        assertEquals([CostType.BLUE], new Card(costString: "U").cost)
    }

    @Test
    void getColors() {
        assertEquals([ManaColor.BLUE, ManaColor.BLACK, ManaColor.RED], new Card(costString: "2UUBBRR").colors)
        // this cost string is deliberately ordered "weird"
        assertEquals([ManaColor.BLUE, ManaColor.BLACK, ManaColor.RED], new Card(costString: "RUBR2UB").colors)
    }

    @Test
    void isCreature() {
        assertTrue(new Card(type: "Creature").creature)
        assertTrue(new Card(type: "Legendary Creature").creature)
        assertTrue(new Card(type: "Enchantment Creature").creature)
        assertTrue(new Card(type: "Legendary Enchantment Creature").creature)
        assertFalse(new Card(type: "Instant").creature)
        assertFalse(new Card(type: "Enchantment").creature)
        assertFalse(new Card(type: "Land").creature)
    }

}
