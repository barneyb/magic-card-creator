package com.barneyb.magic.creator.descriptor

import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
@TupleConstructor
class CardSet extends AbstractList<Card> {

    String name
    String footer

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

}
