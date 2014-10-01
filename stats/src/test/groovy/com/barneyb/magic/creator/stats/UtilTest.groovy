package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import org.junit.Test

import static com.barneyb.magic.creator.api.ManaColor.*
import static org.junit.Assert.*
/**
 *
 *
 * @author barneyb
 */
class UtilTest {

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    @Test
    void cmc() {
        assert 0 == Util.cmc(symbolFactory.getCost(''))
        assert 0 == Util.cmc(symbolFactory.getCost('0'))
        assert 1 == Util.cmc(symbolFactory.getCost('1'))
        assert 1 == Util.cmc(symbolFactory.getCost('r'))
        assert 1 == Util.cmc(symbolFactory.getCost('{r/w}'))
        assert 2 == Util.cmc(symbolFactory.getCost('rw'))
        assert 1 == Util.cmc(symbolFactory.getCost('xg'))
        assert 6 == Util.cmc(symbolFactory.getCost('4gg'))
    }

    @Test
    void devotion() {
        assert 0 == Util.devotion(symbolFactory.getCost(''))
        assert 0 == Util.devotion(symbolFactory.getCost('0'))
        assert 0 == Util.devotion(symbolFactory.getCost('1'))
        assert 1 == Util.devotion(symbolFactory.getCost('r'))
        assert 2 == Util.devotion(symbolFactory.getCost('{r/w}'))
        assert 2 == Util.devotion(symbolFactory.getCost('rw'))
        assert 1 == Util.devotion(symbolFactory.getCost('xg'))
        assert 2 == Util.devotion(symbolFactory.getCost('4gg'))
    }

    @Test
    void devotionColor() {
        assert 1 == Util.devotion(symbolFactory.getCost('r'), RED)
        assert 0 == Util.devotion(symbolFactory.getCost('r'), WHITE)
        assert 1 == Util.devotion(symbolFactory.getCost('{r/w}'), RED)
        assert 1 == Util.devotion(symbolFactory.getCost('{r/w}'), WHITE)
        assert 0 == Util.devotion(symbolFactory.getCost('{r/w}'), GREEN)
        assert 1 == Util.devotion(symbolFactory.getCost('rw'), RED)
        assert 1 == Util.devotion(symbolFactory.getCost('rw'), WHITE)
        assert 2 == Util.devotion(symbolFactory.getCost('4gg'), GREEN)
    }

    @Test
    void getKey() {
        assertEquals("cat", Util.toKey("Cat"))
        assertEquals("cat-dog", Util.toKey("Cat Dog"))
        assertEquals("cat-dog-echo", Util.toKey("Cat Dog Echo"))
        assertEquals("caps-at-front", Util.toKey("CAPS At Front"))
        assertEquals("ending-caps", Util.toKey("Ending CAPS"))
        assertEquals("central-caps-segment", Util.toKey("Central CAPS Segment"))
    }

    @Test
    void getLabel() {
        assertEquals("Cat", Util.toLabel("Cat"))
        assertEquals("Cat Dog", Util.toLabel("CatDog"))
        assertEquals("Cat Dog Echo", Util.toLabel("CatDogEcho"))
        assertEquals("CAPS At Front", Util.toLabel("CAPSAtFront"))
        assertEquals("Ending CAPS", Util.toLabel("EndingCAPS"))
        assertEquals("Central CAPS Segment", Util.toLabel("CentralCAPSSegment"))
    }

}
