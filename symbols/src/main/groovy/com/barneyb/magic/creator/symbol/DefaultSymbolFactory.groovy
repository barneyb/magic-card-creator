package com.barneyb.magic.creator.symbol

import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.api.SymbolGroup

/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolFactory implements SymbolFactory {

    @Override
    Symbol getSymbol(String symbol) {
        symbol = symbol.toUpperCase()
        if (symbol.startsWith('{') && symbol.endsWith('}')) {
            symbol = symbol.substring(1, symbol.length() - 1)
        }
        if (symbol.matches(~/\d\d?/)) {
            new DefaultSymbol(symbol, [ManaColor.COLORLESS])
        } else if (symbol == 'X') {
            new DefaultSymbol(symbol, [ManaColor.COLORLESS])
        } else if (symbol.matches(~/[WUBRG]\/[WUBRG]/)) {
            new DefaultSymbol(symbol, [ManaColor.fromSymbol(symbol.substring(0, 1)), ManaColor.fromSymbol(symbol.substring(2, 3))], true)
        } else if (symbol.matches(~/2\/[WUBRG]/)) {
            new DefaultSymbol(symbol, [ManaColor.COLORLESS, ManaColor.fromSymbol(symbol.substring(2, 3))])
        } else if (symbol.matches(~/[WUBRG]\/P/)) {
            new DefaultSymbol(symbol, [ManaColor.fromSymbol(symbol.substring(0, 1))])
        } else if (symbol == 'W') {
            new DefaultSymbol(symbol, [ManaColor.WHITE])
        } else if (symbol == 'U') {
            new DefaultSymbol(symbol, [ManaColor.BLUE])
        } else if (symbol == 'B') {
            new DefaultSymbol(symbol, [ManaColor.BLACK])
        } else if (symbol == 'R') {
            new DefaultSymbol(symbol, [ManaColor.RED])
        } else if (symbol == 'G') {
            new DefaultSymbol(symbol, [ManaColor.GREEN])
        } else if (symbol == 'T') {
            new DefaultSymbol(symbol, [])
        } else if (symbol == 'Q') {
            new DefaultSymbol(symbol, [])
        } else {
            throw new IllegalArgumentException("No '$symbol' symbol is known.")
        }
    }

    @Override
    SymbolGroup getCost(String cost) {
        getCost(parts(cost))
    }

    @Override
    SymbolGroup getCost(Collection<String> symbols) {
        def l = symbols
            .collect(this.&getSymbol)
        def cl = l.findAll {
            it.symbol != 'X' && it.colors == [ManaColor.COLORLESS]
        }
        if (cl.size() > 1) {
            l.removeAll(cl)
            l << getSymbol(cl*.symbol.collect {
                it.toInteger()
            }.sum().toString())
        }
        new DefaultSymbolGroup(l)
    }

    List<String> parts(String cost) {
        def result = []
        StringBuilder hunk = null
        cost.each { c ->
            if (c == '{') {
                hunk = new StringBuilder()
            } else if (c == '}') {
                result << hunk.toString()
                //noinspection GrReassignedInClosureLocalVar
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
