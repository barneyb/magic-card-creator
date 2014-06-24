package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static org.junit.Assert.*

/**
 *
 * @author bboisvert
 */
class CardTest {

    @Test
    void whitespaceTrimming() {
        def c = new Card(
            title: " Tim ",
            costString: " 2u ",
            artwork: "  ./tim.jpg ",
            type: "  creature  ",
            subtype: "  wizard  ",
            power: " 1 ",
            toughness: " 1 ",
            body: " :P ",
            artist: " Bacon McPiggers ",
        )
        assertEquals("Tim", c.title)
        assertEquals("2u", c.costString)
        assertEquals("./tim.jpg", c.artwork)
        assertEquals("creature", c.type)
        assertEquals("wizard", c.subtype)
        assertEquals(":P", c.body)
        assertEquals("1", c.power)
        assertEquals("1", c.toughness)
        assertEquals("Bacon McPiggers", c.artist)
    }

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
