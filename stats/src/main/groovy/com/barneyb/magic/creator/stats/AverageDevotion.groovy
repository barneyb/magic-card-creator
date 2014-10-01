package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
/**
 *
 *
 * @author barneyb
 */
class AverageDevotion extends BaseNumeric {

    AverageDevotion(CardSet cs) {
        super(Util.mean(cs, {
            if (! it.castingCost) {
                return false
            }
            def colors = it.castingCost*.colors.flatten()
            colors.size() > 1 || ! colors.contains(ManaColor.COLORLESS)
        }, Util.&devotion))
    }

}
