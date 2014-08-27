package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static junit.framework.Assert.assertEquals

/**
 *
 *
 * @author barneyb
 */
class CardSetImporterTest {

    @Test
    void allInOne() {
        def cs = new CardSetImporter().fromUrl(getClass().classLoader.getResource("all-in-one.xml"))
        assertEquals("All In One", cs.title)
        assertEquals("AiO", cs.key)
        assertEquals("\u00A9 Wizards of the Coast", cs.copyright)
        assertEquals([
            "Nykthos, Shrine to Nyx",
            "Lightning Bolt",
            "White Knight",
            "Garruk, Apex Predator",
            "Guul Draz Assassin",
            "Dimir Guildgate",
            "Transguild Courier",
            "Paralyze",
        ], cs.cards*.title)
        // todo: assert ALL the things!
    }

}
