package com.barneyb.magic.creator.descriptor.markdown

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CreatureCard

import static org.junit.Assert.*

/**
 *
 * @author bboisvert
 */
class Assert {

    static assertCard(Card e, Card a) {
        try {
            assertEquals("title doesn't match", e.title, a.title)
            assertEquals("castingCost doesn't match", e.castingCost, a.castingCost)
            assertEquals("artwork doesn't match", e.artwork.url, a.artwork.url)
            assertEquals("type doesn't match", e.typeParts, a.typeParts)
            assertEquals("subtype doesn't match", e.subtypeParts, a.subtypeParts)
            assertEquals("body doesn't match", e.rulesText + e.flavorText, a.rulesText + a.flavorText)
            if (e instanceof CreatureCard) {
                assert a instanceof CreatureCard
                assertEquals("power doesn't match", e.power, a.power)
                assertEquals("toughness doesn't match", e.toughness, a.toughness)
            }
            assertEquals("artist doesn't match", e.artwork.artist, a.artwork.artist)
        } catch (AssertionError ae) {
            println "expected: $e"
            println "  actual: $a"
            throw ae
        }
    }

    static assertCardSet(CardSet expected, CardSet actual) {
        try {
            assertEquals(expected, actual)
        } catch (AssertionError ae) {
            assertEquals("title doesn't match", expected.title, actual.title)
            assertEquals("copyright doesn't match", expected.copyright, actual.copyright)
            assertEquals("cardsInSet doesn't match", expected.cardsInSet, actual.cardsInSet)
            assertEquals("titles don't match", expected.cards*.title, actual.cards*.title)
            expected.cards.eachWithIndex { it, i ->
                assertCard(it, actual.cards.get(i))
            }
            throw ae
        }
    }

}
