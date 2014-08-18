package com.barneyb.magic.creator.descriptor

import groovy.transform.EqualsAndHashCode

/**
 *
 * @author bboisvert
 */
@EqualsAndHashCode(includes = ["symbol"])
class CostType implements AssetKeyed, Comparable<CostType> {

    static final CostType COLORLESS_X = new CostType('x', ManaColor.COLORLESS)
    static final CostType COLORLESS_0 = new CostType('0', ManaColor.COLORLESS)
    static final CostType COLORLESS_1 = new CostType('1', ManaColor.COLORLESS)
    static final CostType COLORLESS_2 = new CostType('2', ManaColor.COLORLESS)
    static final CostType COLORLESS_3 = new CostType('3', ManaColor.COLORLESS)
    static final CostType COLORLESS_4 = new CostType('4', ManaColor.COLORLESS)
    static final CostType COLORLESS_5 = new CostType('5', ManaColor.COLORLESS)
    static final CostType COLORLESS_6 = new CostType('6', ManaColor.COLORLESS)
    static final CostType COLORLESS_7 = new CostType('7', ManaColor.COLORLESS)
    static final CostType COLORLESS_8 = new CostType('8', ManaColor.COLORLESS)
    static final CostType COLORLESS_9 = new CostType('9', ManaColor.COLORLESS)
    static final CostType WHITE = new CostType('w', ManaColor.WHITE)
    static final CostType BLUE = new CostType('u', ManaColor.BLUE)
    static final CostType BLACK = new CostType('b', ManaColor.BLACK)
    static final CostType RED = new CostType('r', ManaColor.RED)
    static final CostType GREEN = new CostType('g', ManaColor.GREEN)
    static final CostType TAP = new CostType('t', [])
    static final CostType UNTAP = new CostType('q', [])

    final String symbol
    final Collection<ManaColor> colors
    final String assetKey

    private def CostType(String symbol, ManaColor color, String assetKey=symbol) {
        this(symbol, [color], assetKey)
    }

    private def CostType(String symbol, Collection<ManaColor> colors, String assetKey=symbol) {
        this.symbol = symbol
        this.colors = colors
        this.assetKey = assetKey
    }

    static CostType fromSymbol(String symbol) {
        def t = _fromSymbol(symbol)
        if (t == null) {
            throw new IllegalArgumentException("The '$symbol' symbol is not a recognized cost symbol.")
        }
        t
    }

    private static CostType _fromSymbol(String symbol) {
        symbol = symbol.toLowerCase()
        if (symbol.matches(~/\d\d?/)) {
            new CostType(symbol, ManaColor.COLORLESS)
        } else if (symbol == 'x') {
            new CostType(symbol, ManaColor.COLORLESS)
        } else if (symbol.matches(~/[wubrg]\/[wubrg]/)) {
            new CostType(symbol, [ManaColor.fromSymbol(symbol.substring(0, 1)), ManaColor.fromSymbol(symbol.substring(2, 3))])
        } else if (symbol.matches(~/2\/[wubrg]/)) {
            new CostType(symbol, [ManaColor.COLORLESS, ManaColor.fromSymbol(symbol.substring(2, 3))])
        } else if (symbol.matches(~/[wubrg]\/p/)) {
            new CostType(symbol, [ManaColor.fromSymbol(symbol.substring(0, 1))])
        } else if (symbol == 'w') {
            new CostType(symbol, ManaColor.WHITE)
        } else if (symbol == 'u') {
            new CostType(symbol, ManaColor.BLUE)
        } else if (symbol == 'b') {
            new CostType(symbol, ManaColor.BLACK)
        } else if (symbol == 'r') {
            new CostType(symbol, ManaColor.RED)
        } else if (symbol == 'g') {
            new CostType(symbol, ManaColor.GREEN)
        } else if (symbol == 't') {
            new CostType(symbol, [])
        } else if (symbol == 'q') {
            new CostType(symbol, [])
        } else {
            null
        }
    }

    static boolean isSymbol(String symbol) {
        _fromSymbol(symbol) != null
    }

    static List<CostType> sort(Collection<CostType> all) {
        def byColor = all.groupBy {
            it.colors.size() == 0 ? null : it.colors.first()
        }
        def nulls = byColor.remove(null)
        def result = []
        ManaColor.sort(byColor.keySet()).each { c ->
            result.addAll byColor[c].sort()
        }
        if (nulls != null) {
            result.addAll nulls.sort()
        }
        result
    }

    @Override
    int compareTo(CostType other) {
        // 'x' is always first
        if (symbol == 'x') {
            other.symbol == 'x' ? 0 : -1
        } else if (other.symbol == 'x') {
            1
        } else if (colors.size() != other.colors.size()) {
            other.colors.size().compareTo(colors.size()) // desc
        } else {
            symbol.compareTo(other.symbol) // asc
        }
    }

    @Override
    String toString() {
        "{$symbol}"
    }

}