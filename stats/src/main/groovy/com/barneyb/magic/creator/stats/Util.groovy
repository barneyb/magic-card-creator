package com.barneyb.magic.creator.stats

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
/**
 *
 *
 * @author barneyb
 */
final class Util {

    private Util() {}

    static float mean(CardSet cs, Closure<Boolean> filter, Closure<Number> num) {
        def cards = cs.cards.findAll(filter)
        cards.collect(num).sum(0) / cards.size()
    }

    static int cmc(Card c) {
        cmc(c.castingCost)
    }

    static int cmc(SymbolGroup sg) {
        sg.findAll {
            it.colored
        }.sum 0, { Symbol it ->
            (it.colors.size() > 1 || it.color != ManaColor.COLORLESS) ? 1 : it.symbol.isInteger() ? it.symbol.toInteger() : 0
        }
    }

    static int devotion(Card c, ManaColor color = null) {
        devotion(c.castingCost, color)
    }

    static int devotion(SymbolGroup sg, ManaColor color = null) {
        sg.findAll {
            it.colored && (it.colors.size() > 1 || it.color != ManaColor.COLORLESS)
        }.sum 0, { Symbol it ->
            color == null ? (it.colors - ManaColor.COLORLESS).size() : it.colors.contains(color) ? 1 : 0
        }
    }

    static String toLabel(String camelName) {
        camelName.collectReplacements { char it ->
            Character.isUpperCase(it) ? ' ' + it : null
        }.trim().replaceAll(/([A-Z]) (?=[A-Z]( |$))/, '$1')
    }

    static String toKey(String label) {
        label.toLowerCase().replaceAll(/[^a-z0-9]+/, '-')
    }

}
