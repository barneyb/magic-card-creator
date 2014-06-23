package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertSame

/**
 *
 * @author bboisvert
 */
class CardSetTest {

    @Test
    void number() {
        def set = new CardSet()
        set << new Card("Barney")
        def s = new Card("Sally")
        assertNull(s.set)
        set << s
        assertSame(set, s.set)
        set << new Card("Brenna")
        assertEquals(2, set.getCardOfSet(s))
        set.add(0, new Card("Ashley"))
        assertEquals(3, set.getCardOfSet(s))
    }

    @Test
    void size() {
        def set = new CardSet()
        set << new Card("Barney")
        assertEquals(1, set.cardsInSet)
        set << new Card("Brenna")
        assertEquals(2, set.cardsInSet)
    }

}
