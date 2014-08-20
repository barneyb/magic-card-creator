package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static com.barneyb.magic.creator.descriptor.ManaColor.*
import static org.junit.Assert.*

/**
 *
 * @author bboisvert
 */
class ManaColorOrderingTest {

    protected void check(List<ManaColor> colors) {
        colors = colors.asImmutable()
        assertEquals("failed to preserve $colors", colors, sort(colors))
        def rev = colors.reverse()
        assertEquals("failed to reverse $rev", colors, sort(rev))
        def shuf = []
        shuf.addAll(colors)
        10.times {
            Collections.shuffle(shuf)
            assertEquals("failed to unshuffle $shuf", colors, sort(shuf))
        }
    }

    @Test
    void pairs() {
        check([GREEN, WHITE]) // chronicler of heros
        check([BLACK, RED]) // kragma warcaller
        check([RED, WHITE]) // akroan hoplite
        check([WHITE, BLUE]) // aethermage's touch
        check([WHITE, BLACK]) // agent of masks
        check([GREEN, BLUE]) // assault zeppilid
        check([RED, GREEN]) // borborygmos
        check([BLUE, RED]) // cerebral vortex
        check([BLUE, BLACK]) // circu, dimir lobotomist
        check([BLACK, GREEN]) // drooling groodion
    }

    @Test
    void chords() {
        check([WHITE, BLUE, BLACK]) // dromar's charm
        check([BLUE, BLACK, RED]) // grixis charm
        check([BLACK, RED, GREEN]) // darigaaz's charm
        check([RED, GREEN, WHITE]) // naya charm
        check([GREEN, WHITE, BLUE]) // bant charm
    }

    @Test
    void wedges() {
        check([RED, WHITE, BLUE]) // numot
        check([BLACK, GREEN, WHITE]) // teneb
        check([BLUE, RED, GREEN]) // intet
        check([WHITE, BLACK, RED]) // oros
        check([GREEN, BLUE, BLACK]) // vorosh
    }

    @Test
    void four() {
        check([BLACK, RED, GREEN, WHITE]) // dune-brood nephilim
        check([BLUE, BLACK, RED, GREEN]) // glint-eye nephilim
        check([RED, GREEN, WHITE, BLUE]) // ink-treader nephilim
        check([GREEN, WHITE, BLUE, BLACK]) // witch-maw nephilim
        check([WHITE, BLUE, BLACK, RED]) // yore-tiller nephilim
    }

    @Test
    void failedShuffles() {
        // no idea why this particular starting point fails, but it does.
        assertEquals([BLACK, RED, GREEN, WHITE], sort([RED, WHITE, BLACK, GREEN]))
        assertEquals([RED, WHITE, BLUE], sort([RED, BLUE, WHITE]))
        assertEquals([RED, WHITE, BLUE], sort([BLUE, RED, WHITE]))
        assertEquals([WHITE, BLACK, RED], sort([RED, WHITE, BLACK]))
        assertEquals([COLORLESS, COLORLESS, WHITE, WHITE, BLACK, BLACK, RED, RED], sort([COLORLESS, WHITE, RED, RED, COLORLESS, WHITE, BLACK, BLACK] ))
    }

    @Test
    void allFive() {
        check([WHITE, BLUE, BLACK, RED, GREEN]) // chromanticore
    }

    @Test
    void dupesAndColorless() {
        check([COLORLESS, RED, WHITE, BLUE]) // lightning angel
        check([COLORLESS, WHITE, WHITE, BLACK, BLACK]) // angel of dispair
        check([COLORLESS, BLUE, BLACK, BLACK, RED]) // nicol bolas, planeswalker
        check([COLORLESS, COLORLESS, WHITE, WHITE, BLACK, BLACK, RED, RED])
    }

    @Test
    void mutate() {
        // angel of dispair
        def first = [BLACK, WHITE, COLORLESS, WHITE, BLACK]
        def dupe = new ArrayList(first)
        def expected = [COLORLESS, WHITE, WHITE, BLACK, BLACK]
        // sort and return new list
        def result = sort(first)
        assertEquals("sorting didn't work", expected, result)
        assertEquals("first has changed", first, dupe)
        assertNotSame("sorting didn't create new", first, result)

        // sort in place
        result = sort(first, true)
        assertTrue("sort created new", first.is(result))
        assertEquals("sorting didn't work", expected, first)

        // can't sort non-list in place
        // lightning angel
        first = [WHITE, BLUE, RED, COLORLESS] as Set
        expected = [COLORLESS, RED, WHITE, BLUE]
        result = sort(first, true)
        assertEquals("sorting didn't work", expected, result)
        assertNotSame("sorting didn't create new", first, result)
    }

}
