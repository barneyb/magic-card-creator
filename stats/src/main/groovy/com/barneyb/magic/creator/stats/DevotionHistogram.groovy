package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
/**
 *
 *
 * @author barneyb
 */
class DevotionHistogram extends BaseHistogram {

    DevotionHistogram(CardSet cs) {
        super(cs.cards.findAll {
            it.castingCost
        }.collect(Util.&devotion).countBy { it }.sort())
    }

}
