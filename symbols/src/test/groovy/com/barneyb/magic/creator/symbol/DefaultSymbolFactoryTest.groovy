package com.barneyb.magic.creator.symbol
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolFactoryTest {

    static final Symbol COLORLESS_2 = new DefaultSymbol('2', ManaColor.COLORLESS)
    static final Symbol BLUE = new DefaultSymbol('u', ManaColor.BLUE)
    static final Symbol BLACK = new DefaultSymbol('b', ManaColor.BLACK)
    static final Symbol RED = new DefaultSymbol('r', ManaColor.RED)
    static final Symbol MONO_BLUE = new DefaultSymbol('2/u', [ManaColor.COLORLESS, ManaColor.BLUE])
    static final Symbol WHITE_GREEN = new DefaultSymbol('w/g', [ManaColor.WHITE, ManaColor.GREEN])
    static final Symbol PHYREXIAN_GREEN = new DefaultSymbol('g/p', ManaColor.GREEN)


    DefaultSymbolFactory factory

    @Before
    void _makeFactory() {
        factory = new DefaultSymbolFactory()
    }

    @Test
    void casing() {
        assertEquals('U', factory.getSymbol("u").symbol)
        assertEquals('U', factory.getSymbol("U").symbol)
    }

    @Test
    void braces() {
        assertEquals('{U}', factory.getSymbol("U").toString())
        assertEquals('{U}', factory.getSymbol("{U}").toString())
    }

    @Test
    void noErrors() {
        [
            "colored": ['w', 'u', 'b', 'r', 'g'],
            "colorless": ['x'] + (0..20)*.toString(),
            "tap": ['t', 'q'],
            "hybrid": ['w/u', 'w/b', 'u/b', 'u/r', 'b/r', 'b/g', 'r/g', 'r/w', 'g/w', 'g/u'],
            "mono-hybrid": ['2/w', '2/u', '2/b', '2/r', '2/g'],
            "phyrexian": ['w/p', 'u/p', 'b/p', 'r/p', 'g/p'],
        ].each { n, is ->
            println n
            is.each {
                println "  $it : ${factory.getSymbol(it.toLowerCase())} : ${factory.getSymbol('{' + it.toUpperCase() + '}')}"
            }
        }
    }

    @Test
    void nicolBolas() {
        assertEquals([COLORLESS_2, BLUE, BLUE, BLACK, BLACK, RED, RED], factory.getCost("2uubbrr"))
        // this cost string is deliberately ordered "weird"
        assertEquals([RED, BLUE, BLACK, RED, COLORLESS_2, BLUE, BLACK], factory.getCost("RUBR2UB"))
    }

    @Test
    void hybrids() {
        assertEquals([
            COLORLESS_2,
            MONO_BLUE,
            WHITE_GREEN,
            PHYREXIAN_GREEN
        ], factory.getCost("2{2/u}{w/g}{g/p}"))
    }

    @Test
    void parts() {
        assertEquals(['w', 'u', 'b', 'r', 'g'], factory.parts("wubrg"))
        assertEquals(['2', '2/u', 'w/g', 'g/p'], factory.parts("2{2/u}{w/g}{g/p}"))
    }

}
