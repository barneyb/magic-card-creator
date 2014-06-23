package com.barneyb.magic.creator.descriptor

/**
 *
 * @author bboisvert
 */
class CostParser {

    static List<CostType> parse(String cost) {
        cost.collect {
            CostType.fromKey(it.toLowerCase())
        }
    }

}
