package com.barneyb.magic.creator.symbol
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory
import org.junit.Test

import static org.junit.Assert.*
/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolGroupTest {

    static final SymbolFactory factory = new DefaultSymbolFactory()

    static final Symbol COLORLESS_1 = factory.getSymbol('1')
    static final Symbol COLORLESS_2 = factory.getSymbol('2')
    static final Symbol COLORLESS_6 = factory.getSymbol('6')
    static final Symbol COLORLESS_X = factory.getSymbol('x')
    static final Symbol TAP = factory.getSymbol('t')
    static final Symbol WHITE = factory.getSymbol('w')
    static final Symbol BLUE = factory.getSymbol('u')
    static final Symbol BLACK = factory.getSymbol('b')
    static final Symbol RED = factory.getSymbol('r')
    static final Symbol GREEN = factory.getSymbol('g')

    protected void check(List<Symbol> colors) {
        colors = new DefaultSymbolGroup(colors.asImmutable())
        assertEquals("failed to preserve $colors", colors, colors.sort())
        def rev = new DefaultSymbolGroup(colors.reverse())
        assertEquals("failed to reverse $rev", colors, rev.sort())
        def shuf = new DefaultSymbolGroup([])
        shuf.addAll(colors)
        10.times {
            Collections.shuffle(shuf)
            assertEquals("failed to unshuffle $shuf", colors, shuf.sort())
        }
    }

    @Test
    void barney() {
        check([COLORLESS_1, GREEN, BLUE])
    }

    @Test
    void hammerOfPurphoros() {
        check([COLORLESS_1, RED, RED]) // cast
        check([COLORLESS_2, RED, TAP]) // activate
    }

    @Test
    void forceOfNature() {
        check([COLORLESS_2, GREEN, GREEN, GREEN, GREEN])
    }

    @Test
    void drainLife() {
        check([COLORLESS_X, COLORLESS_1, BLACK])
    }

    @Test
    void nicolBolas() {
        check([COLORLESS_2, BLUE, BLUE, BLACK, BLACK, RED, RED])
    }

    @Test
    void garbage() {
        check([COLORLESS_X, COLORLESS_6, WHITE, BLUE, BLACK, RED, GREEN])
    }

    @Test
    void akroanHoplite() {
        check([RED, WHITE])
    }

}
