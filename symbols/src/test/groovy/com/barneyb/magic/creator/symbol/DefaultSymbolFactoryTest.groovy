package com.barneyb.magic.creator.symbol

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolFactoryTest {

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

}
