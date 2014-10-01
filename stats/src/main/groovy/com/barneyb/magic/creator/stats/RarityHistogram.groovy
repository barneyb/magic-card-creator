package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.Rarity

/**
 *
 *
 * @author barneyb
 */
class RarityHistogram extends BaseHistogram {

    RarityHistogram(CardSet cs) {
        super(Rarity.enumConstants.collectEntries {
            [it, 0]
        } + cs.cards.countBy { it.rarity ?: Rarity.COMMON })
    }

}
