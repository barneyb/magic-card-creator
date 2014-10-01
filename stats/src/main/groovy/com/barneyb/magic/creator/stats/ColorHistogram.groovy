package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
/**
 *
 *
 * @author barneyb
 */
class ColorHistogram extends BaseHistogram {

    ColorHistogram(CardSet cs) {
        super(ManaColor.enumConstants.collectEntries {
            [it, 0]
        } + cs.cards*.colors.flatten().countBy { it })
    }

}
