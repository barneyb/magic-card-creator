package com.barneyb.magic.creator.symbol

import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory

/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolFactory implements SymbolFactory {

    @Override
    Symbol getSymbol(String symbol) {
        symbol = symbol.toLowerCase()
        if (symbol.matches(~/\d\d?/)) {
            new DefaultSymbol(symbol, [ManaColor.COLORLESS])
        } else if (symbol == 'x') {
            new DefaultSymbol(symbol, [ManaColor.COLORLESS])
        } else if (symbol.matches(~/[wubrg]\/[wubrg]/)) {
            new DefaultSymbol(symbol, [ManaColor.fromSymbol(symbol.substring(0, 1)), ManaColor.fromSymbol(symbol.substring(2, 3))])
        } else if (symbol.matches(~/2\/[wubrg]/)) {
            new DefaultSymbol(symbol, [ManaColor.COLORLESS, ManaColor.fromSymbol(symbol.substring(2, 3))])
        } else if (symbol.matches(~/[wubrg]\/p/)) {
            new DefaultSymbol(symbol, [ManaColor.fromSymbol(symbol.substring(0, 1))])
        } else if (symbol == 'w') {
            new DefaultSymbol(symbol, [ManaColor.WHITE])
        } else if (symbol == 'u') {
            new DefaultSymbol(symbol, [ManaColor.BLUE])
        } else if (symbol == 'b') {
            new DefaultSymbol(symbol, [ManaColor.BLACK])
        } else if (symbol == 'r') {
            new DefaultSymbol(symbol, [ManaColor.RED])
        } else if (symbol == 'g') {
            new DefaultSymbol(symbol, [ManaColor.GREEN])
        } else if (symbol == 't') {
            new DefaultSymbol(symbol, [])
        } else if (symbol == 'q') {
            new DefaultSymbol(symbol, [])
        } else {
            throw new IllegalArgumentException("No '$symbol' symbol is known.")
        }
    }

}
