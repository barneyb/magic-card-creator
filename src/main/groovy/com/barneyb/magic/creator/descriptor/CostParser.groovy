package com.barneyb.magic.creator.descriptor

import static com.barneyb.magic.creator.descriptor.CostType.*

/**
 *
 * @author bboisvert
 */
class CostParser {

    static List<CostType> parse(String cost, boolean allowTap=false) {
        def l = cost.collect {
            fromKey(it.toLowerCase())
        }.findAll {
            it != null && (allowTap || it != TAP)
        }
        def cl = l.findAll {
            it != COLORLESS_X && it.colors == [ManaColor.COLORLESS]
        }
        if (cl.size() > 1) {
            l.removeAll(cl)
            l << fromKey(cl*.assetKey*.toInteger().sum().toString())
        }
        l.sort()
    }

}
