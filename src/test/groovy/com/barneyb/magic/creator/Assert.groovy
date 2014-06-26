package com.barneyb.magic.creator
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.descriptor.Card
import com.barneyb.magic.creator.descriptor.CardSet

import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class Assert {

    static assertRenderModel(RenderModel e, RenderModel a) {
        try {
            assertEquals(e, a)
        } catch (AssertionError ae) {
            assertEquals("frame doesn't match", e.frame, a.frame)
            assertEquals("title doesn't match", e.title, a.title)
            assertEquals("cost doesn't match", e.cost, a.cost)
            assertEquals("artwork doesn't match", e.artwork, a.artwork)
            assertEquals("type doesn't match", e.type, a.type)
            assertEquals("body doesn't match", e.body, a.body)
            assertEquals("powerToughness doesn't match", e.powerToughness, a.powerToughness)
            assertEquals("whiteFooterText doesn't match", e.whiteFooterText, a.whiteFooterText)
            assertEquals("artist doesn't match", e.artist, a.artist)
            assertEquals("footer doesn't match", e.footer, a.footer)
            throw ae
        }
    }

    static assertCard(Card e, Card a) {
        try {
            assertEquals("title doesn't match", e.title, a.title)
            assertEquals("costString doesn't match", e.costString, a.costString)
            assertEquals("artwork doesn't match", e.artwork, a.artwork)
            assertEquals("type doesn't match", e.type, a.type)
            assertEquals("subtype doesn't match", e.subtype, a.subtype)
            assertEquals("body doesn't match", e.body, a.body)
            assertEquals("power doesn't match", e.power, a.power)
            assertEquals("toughness doesn't match", e.toughness, a.toughness)
            assertEquals("artist doesn't match", e.artist, a.artist)
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
            assertEquals("name doesn't match", expected.name, actual.name)
            assertEquals("footer doesn't match", expected.footer, actual.footer)
            assertEquals("cardsInSet doesn't match", expected.cardsInSet, actual.cardsInSet)
            assertEquals("titles don't match", expected*.title, actual*.title)
            expected.eachWithIndex { it, i ->
                assertCard(it, actual.get(i))
            }
            throw ae
        }
    }

}
