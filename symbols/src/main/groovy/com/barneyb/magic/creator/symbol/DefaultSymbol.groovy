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
    final boolean hybrid

    def DefaultSymbol(String symbol, ManaColor color) {
        this(symbol, [color])
    }

    def DefaultSymbol(String symbol, List<ManaColor> colors, boolean hybrid=false) {
        this.symbol = symbol.toUpperCase()
        this.colors = colors
        this.hybrid = hybrid
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
