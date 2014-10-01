package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
/**
 *
 *
 * @author barneyb
 */
class CreatureTypeHistogram extends BaseHistogram {

    CreatureTypeHistogram(CardSet cs) {
        super(cs.cards.findAll {
            it.isType('creature')
        }*.subtypeParts.flatten().countBy { it })
    }

}
