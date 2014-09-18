package com.barneyb.magic.creator.symbol

import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolTest {

    static final SymbolFactory factory = new DefaultSymbolFactory()

    static final Symbol WHITE = factory.getSymbol('w')
    static final Symbol BLUE = factory.getSymbol('u')
    static final Symbol BLACK = factory.getSymbol('b')
    static final Symbol RED = factory.getSymbol('r')
    static final Symbol GREEN = factory.getSymbol('g')

    Symbol s(String s) {
        factory.getSymbol(s)
    }

    @Test
    void tapUntap() {
        ['t', 'q'].each {
            assert WHITE < s(it)
            assert GREEN < s(it)
            assert s('3') < s(it)
            assert s('x') < s(it)
            assert s('r/w') < s(it)
        }
    }

    @Test
    void compareByOnes() {
        assert WHITE < BLUE
        assert BLUE < BLACK
        assert BLACK < RED
        assert RED < GREEN
        assert GREEN < WHITE
    }

    @Test
    void compareByTwos() {
        assert WHITE < BLACK
        assert BLUE < RED
        assert BLACK < GREEN
        assert RED < WHITE
        assert GREEN < BLUE
    }

    @Test
    void compareByThrees() {
        assert WHITE > RED
        assert BLUE > GREEN
        assert BLACK > WHITE
        assert RED > BLUE
        assert GREEN > BLACK
    }

    @Test
    void compareByFours() {
        assert WHITE > GREEN
        assert BLUE > WHITE
        assert BLACK > BLUE
        assert RED > BLACK
        assert GREEN > RED
    }

    @Test
    void compareByFives_self() {
        assert WHITE == WHITE
        assert BLUE == BLUE
        assert BLACK == BLACK
        assert RED == RED
        assert GREEN == GREEN
    }

    @Test
    void compareColorlessX() {
        assert s('x') < WHITE
        assert s('x') < GREEN
        assert s('x') < s('2')
    }

    @Test
    void compareColorless() {
        assert s('2') < WHITE
        assert s('2') < GREEN
    }

    @Test
    void compareMonoHybrids() {
        assert s('2/w') < WHITE
        assert s('2/w') < RED
    }

    @Test
    void compareHybrids() {
        assert s('r/w') < WHITE
        assert s('r/w') < RED
        assert s('r/w') < s('r/u')
        assert s('r/g') < s('r/w')
    }

}
