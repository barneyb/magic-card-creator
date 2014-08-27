package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
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

}
