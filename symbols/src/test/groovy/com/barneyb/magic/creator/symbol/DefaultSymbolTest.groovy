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

    SymbolFactory factory = new DefaultSymbolFactory()

    Symbol s(String s) {
        factory.getSymbol(s)
    }

    @Test
    void compareByOnes() {
        assert s('w') < s('u')
        assert s('u') < s('b')
        assert s('b') < s('r')
        assert s('r') < s('g')
        assert s('g') < s('w')
    }

    @Test
    void compareByTwos() {
        assert s('w') < s('b')
        assert s('u') < s('r')
        assert s('b') < s('g')
        assert s('r') < s('w')
        assert s('g') < s('u')
    }

    @Test
    void compareByThrees() {
        assert s('w') > s('r')
        assert s('u') > s('g')
        assert s('r') < s('u')
        assert s('g') < s('b')
        assert s('b') > s('w')
    }

    @Test
    void compareByFours() {
        assert s('w') > s('g')
        assert s('u') > s('r')
        assert s('r') < s('w')
        assert s('g') < s('u')
        assert s('b') > s('b')
    }

    @Test
    void compareByFives_self() {
        assert s('w') == s('w')
        assert s('u') == s('u')
        assert s('r') == s('b')
        assert s('g') == s('r')
        assert s('b') == s('g')
    }

    @Test
    void compareColorlessX() {
        assert s('x') < s('w')
        assert s('x') < s('g')
        assert s('x') < s('2')
    }

    @Test
    void compareColorless() {
        assert s('2') < s('w')
        assert s('2') < s('g')
    }

    @Test
    void compareMonoHybrids() {
        assert s('2/w') < s('w')
        assert s('2/w') < s('r')
    }

    @Test
    void compareHybrids() {
        assert s('r/w') < s('w')
        assert s('r/w') < s('r')
        assert s('r/w') < s('r/u')
        assert s('r/g') < s('r/w')
    }

}
