package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
/**
 *
 *
 * @author barneyb
 */
class CMCHistogram extends BaseHistogram {

    CMCHistogram(CardSet cs) {
        super(cs.cards.findAll {
            it.castingCost
        }.collect(Util.&cmc).countBy { it }.sort())
    }

}
