package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
/**
 *
 *
 * @author barneyb
 */
class CardCount extends BaseNumeric {

    CardCount(CardSet cs) {
        super(cs.cards.size())
    }

}
