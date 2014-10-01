package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
/**
 *
 *
 * @author barneyb
 */
class AverageCMC extends BaseNumeric {

    AverageCMC(CardSet cs) {
        super(Util.mean(cs, { it.castingCost }, Util.&cmc))
    }

}
