package com.barneyb.magic.creator.symbol

import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import groovy.transform.Immutable

/**
 *
 *
 * @author barneyb
 */
@Immutable
class DefaultSymbol implements Symbol {

    String symbol
    List<ManaColor> colors

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
