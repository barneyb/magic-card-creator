package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.Icon
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DefaultCardSet implements CardSet {

    String title = null

    String key = null

    String copyright = null

    List<Card> cards = []

    @Override
    int getCardsInSet() {
        cards?.size() ?: 0
    }

    Icon iconField

    Icon iconSymbol

}
