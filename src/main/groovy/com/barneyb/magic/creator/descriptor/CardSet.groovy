package com.barneyb.magic.creator.descriptor
/**
 *
 * @author bboisvert
 */
class CardSet extends ArrayList<Card> {

    String footer

    int getCardOfSet(Card c) {
        indexOf(c) + 1 // 1-indexed
    }

    int getCardsInSet() {
        size()
    }

}
