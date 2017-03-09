package com.barneyb.magic.creator.util

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
        assertEquals("title doesn't match", e.title, a.title)
        assertEquals("castingCost doesn't match", e.castingCost, a.castingCost)
        assertEquals("color doesn't match", e.colors, a.colors)
        assertEquals("artwork doesn't match", e.artwork?.url, a.artwork?.url)
        assertEquals("type doesn't match", e.typeParts, a.typeParts)
        assertEquals("subtype doesn't match", e.subtypeParts, a.subtypeParts)
        assertEquals("rules text doesn't match", e.rulesText, a.rulesText)
        assertEquals("flavor text doesn't match", e.flavorText, a.flavorText)
        if (e instanceof CreatureCard) {
            assert a instanceof CreatureCard
            assertEquals("power doesn't match", e.power, a.power)
            assertEquals("toughness doesn't match", e.toughness, a.toughness)
        }
        assertEquals("artist doesn't match", e.artwork?.artist, a.artwork?.artist)
        if (e.cardNumber != null) {
            assertEquals("card number doesn't match ($e.title)", e.cardNumber, a.cardNumber)
        }
    }

    static assertCardSet(CardSet expected, CardSet actual) {
        assertEquals("title doesn't match", expected.title, actual.title)
        assertEquals("key doesn't match", expected.key, actual.key)
        assertEquals("copyright doesn't match", expected.copyright, actual.copyright)
        assertEquals("cardsInSet doesn't match", expected.cardsInSet, actual.cardsInSet)
        assertEquals("card titles don't match", expected.cards*.title, actual.cards*.title)
        expected.cards.eachWithIndex { it, i ->
            assertCard(it, actual.cards.get(i))
        }
    }

}
