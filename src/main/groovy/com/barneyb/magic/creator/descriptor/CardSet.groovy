package com.barneyb.magic.creator.descriptor
/**
 *
 * @author bboisvert
 */
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
