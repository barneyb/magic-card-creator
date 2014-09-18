package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.core.DefaultCard
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import org.junit.Before
import org.junit.Test

import static com.barneyb.magic.creator.api.ManaColor.*
/**
 *
 *
 * @author barneyb
 */
class StatsCommandTest {

    StatsCommand cmd

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    @Before
    void _makeCmd() {
        cmd = new StatsCommand()
    }

    @Test
    void cmc() {
        assert 0 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('')))
        assert 0 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('0')))
        assert 1 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('1')))
        assert 1 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('r')))
        assert 1 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('{r/w}')))
        assert 2 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('rw')))
        assert 1 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('xg')))
        assert 6 == cmd.cmc(new DefaultCard(castingCost: symbolFactory.getCost('4gg')))
    }

    @Test
    void devotion() {
        assert 0 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('')))
        assert 0 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('0')))
        assert 0 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('1')))
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('r')))
        assert 2 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('{r/w}')))
        assert 2 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('rw')))
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('xg')))
        assert 2 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('4gg')))
    }

    @Test
    void devotionColor() {
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('r')), RED)
        assert 0 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('r')), WHITE)
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('{r/w}')), RED)
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('{r/w}')), WHITE)
        assert 0 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('{r/w}')), GREEN)
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('rw')), RED)
        assert 1 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('rw')), WHITE)
        assert 2 == cmd.devotion(new DefaultCard(castingCost: symbolFactory.getCost('4gg')), GREEN)
    }

}
