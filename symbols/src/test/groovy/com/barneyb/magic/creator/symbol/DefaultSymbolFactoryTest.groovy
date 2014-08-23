package com.barneyb.magic.creator.symbol

import org.junit.Before
import org.junit.Test

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
    void noErrors() {
        [
            "mana": ['w', 'u', 'b', 'r', 'g'],
            "tap": ['t', 'q'],
            "hybrid": ['w/u', 'w/b', 'u/b', 'u/r', 'b/r', 'b/g', 'r/g', 'r/w', 'g/w', 'g/u'],
            "mono-hybrid": ['2/w', '2/u', '2/b', '2/r', '2/g'],
            "phyrexian": ['w/p', 'u/p', 'b/p', 'r/p', 'g/p'],
        ].each { n, is ->
            println n
            is.each {
                println "  $it : ${factory.getSymbol(it.toLowerCase())} : ${factory.getSymbol(it.toUpperCase())}"
            }
        }
    }

}
