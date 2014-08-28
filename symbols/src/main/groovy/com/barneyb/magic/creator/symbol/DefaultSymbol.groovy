package com.barneyb.magic.creator.symbol
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import groovy.transform.EqualsAndHashCode

/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(includes = "symbol")
class DefaultSymbol implements Symbol {

    final String symbol
    final List<ManaColor> colors

    def DefaultSymbol(String symbol, ManaColor color) {
        this(symbol, [color])
    }

    def DefaultSymbol(String symbol, List<ManaColor> colors) {
        this.symbol = symbol.toUpperCase()
        this.colors = colors
    }

    @Override
    boolean isColored() {
        ! colors.empty
    }

    @Override
    boolean isMultiColor() {
        colors.size() > 1
    }

    @Override
    ManaColor getColor() {
        if (colored) {
            colors.first()
        } else {
            throw new UnsupportedOperationException("You cannot request the color of a non-colored symbol.")
        }
    }

    @Override
    String toString() {
        "{$symbol}"
    }
}
