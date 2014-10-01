package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor

/**
 *
 *
 * @author barneyb
 */
class DevotionToHistogram extends BaseHistogram {

    DevotionToHistogram(CardSet cs) {
        super((ManaColor.enumConstants - ManaColor.COLORLESS).collectEntries {
            [it, cs.cards.findAll { it.castingCost }.sum(0, Util.&devotion.rcurry(it))]
        })
    }

}
