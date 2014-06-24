package com.barneyb.magic.creator.descriptor

import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
@TupleConstructor
class CardSet extends AbstractList<Card> {

    String name = "Unknown Set"
    String footer = "\u00A9 1993-2014 Wizards of the Coast LLC"

    private List<Card> cards = []

    int getCardOfSet(Card c) {
        cards.indexOf(c) + 1 // 1-indexed
    }

    int getCardsInSet() {
        cards.size()
    }

    Card get(String title) {
        def c = cards.find {
            it.title.equalsIgnoreCase(title)
        }
        if (c == null) {
            throw new IllegalArgumentException("This set does not contain a card named '$title'.")
        }
        c
    }

    @Override
    Card get(int i) {
        cards.get(i)
    }

    @Override
    int size() {
        cards.size()
    }

    @Override
    Card set(int i, Card card) {
        def old = remove(i)
        add(i, card)
        old
    }

    @Override
    void add(int i, Card card) {
        if (cards.any { it.title.equalsIgnoreCase(card.title) }) {
            throw new IllegalArgumentException("This set already has a card named '$card.title'.")
        }
        card.set = this
        cards.add(i, card)
    }

    @Override
    Card remove(int i) {
        def old = cards.remove(i)
        if (old != null) {
            old.set = null
        }
        old
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CardSet)) return false
        if (!super.equals(o)) return false

        CardSet cards1 = (CardSet) o

        if (cards != cards1.cards) return false
        if (footer != cards1.footer) return false
        if (name != cards1.name) return false

        return true
    }

    int hashCode() {
        int result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + footer.hashCode()
        result = 31 * result + cards.hashCode()
        return result
    }
}
