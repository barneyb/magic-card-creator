package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.LineBreak
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.NonNormativeText
import com.barneyb.magic.creator.api.RulesText
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import com.barneyb.magic.creator.symbol.DefaultSymbolGroup

/**
 *
 *
 * @author barneyb
 */
class TestMixin {

    def symbolFactory = new DefaultSymbolFactory()

    Symbol s(String symbol) {
        symbolFactory.getSymbol(symbol)
    }

    SymbolGroup sg(String... symbols) {
        new DefaultSymbolGroup(symbols.collect {
            s(it)
        })
    }

    RulesText rt(String s) {
        new DefaultRulesText(s)
    }

    NonNormativeText nnt(String s) {
        new DefaultNonNormativeText(s)
    }

    LineBreak lb() {
        new DefaultLineBreak()
    }

    ManaColor mc(String s) {
        ManaColor.fromSymbol(s)
    }

    List<ManaColor> mcs(String... s) {
        s.collect {
            mc(it)
        }
    }
}
