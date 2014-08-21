package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.ManaColor

import static com.barneyb.magic.creator.descriptor.CostType.*

/**
 *
 * @author bboisvert
 */
class CostParser {

    static List<CostType> parse(String cost, boolean allowTap=false) {
        def l = parts(cost)
            .findAll(CostType.&isSymbol)
            .collect(CostType.&fromSymbol)
            .findAll {
                it != null && (allowTap || (it != TAP && it != UNTAP))
            }
        def cl = l.findAll {
            it != COLORLESS_X && it.colors == [ManaColor.COLORLESS]
        }
        if (cl.size() > 1) {
            l.removeAll(cl)
            l << fromSymbol(cl*.symbol*.toInteger().sum().toString())
        }
        l
    }

    static List<String> parts(String cost) {
        def result = []
        StringBuilder hunk = null
        cost.each { c ->
            if (c == '{') {
                hunk = new StringBuilder()
            } else if (c == '}') {
                result << hunk.toString()
                hunk = null
            } else if (hunk != null) {
                hunk.append(c)
            } else {
                result << c
            }
        }
        result
    }

}
