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
    void cardOfSet() {
        def set = new CardSet()
        set << new Card("Barney")
        def s = new Card("Sally")
        set << s
        set << new Card("Brenna")
        assertEquals(2, set.getCardOfSet(s))
        set.add(0, new Card("Ashley"))
        assertEquals(3, set.getCardOfSet(s))
    }

    @Test
    void cardsInSet() {
        def set = new CardSet()
        set << new Card("Barney")
        assertEquals(1, set.cardsInSet)
        set << new Card("Brenna")
        assertEquals(2, set.cardsInSet)
    }

    @Test
    void manageReverseReference() {
        def set = new CardSet()
        def s = new Card("Sally")

        assertNull(s.set)

        // add / remove
        set.add(s)
        assertSame(set, s.set)
        set.remove(s)

        assertNull(s.set)

        // addAll / removeAll
        set.addAll([s])
        assertSame(set, s.set)
        set.removeAll([s])

        assertNull(s.set)

        // retainAll
        set.add(s)
        assertSame(set, s.set)
        set.retainAll([])

        assertNull(s.set)
    }

    @Test(expected = IllegalArgumentException)
    void duplicateTitle() {
        def set = new CardSet()
        set << new Card("Sally")
        set << new Card("Barney")
        set << new Card("Sally") // BAM!
    }

    @Test
    void getByTitle() {
        def set = new CardSet()
        def s = new Card("Sally")
        def b = new Card("Barney")
        def a = new Card("Andrea")
        set << s << b << a

        assertSame(s, set.get("Sally"))
        assertSame(b, set.get("Barney"))
        assertSame(a, set.get("Andrea"))
    }

}
