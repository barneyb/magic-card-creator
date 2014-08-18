package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static com.barneyb.magic.creator.descriptor.CostType.*
import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class CostParserTest {

    protected def parse(String s) {
        def r = CostParser.parse(s)
        assertEquals("$s was parsed case sensitively", r, CostParser.parse(s.toUpperCase()))
        assertEquals("$s was parsed case sensitively", r, CostParser.parse(s.toLowerCase()))
        r
    }

    @Test
    void colorless() {
        assertEquals([COLORLESS_0], parse("0"))
        assertEquals([COLORLESS_1], parse("1"))
        assertEquals([COLORLESS_2], parse("2"))
        assertEquals([COLORLESS_3], parse("3"))
        assertEquals([COLORLESS_4], parse("4"))
        assertEquals([COLORLESS_5], parse("5"))
        assertEquals([COLORLESS_6], parse("6"))
        assertEquals([COLORLESS_7], parse("7"))
        assertEquals([COLORLESS_8], parse("8"))
        assertEquals([COLORLESS_9], parse("9"))
        assertEquals([new CostType('3', ManaColor.COLORLESS)], parse("12"))
        assertEquals([new CostType('12', ManaColor.COLORLESS)], parse("{12}"))
        assertEquals([COLORLESS_X], parse("x"))
    }

    @Test
    void white() {
        assertEquals([WHITE], parse("w"))
    }

    @Test
    void blue() {
        assertEquals([BLUE], parse("u"))
    }

    @Test
    void black() {
        assertEquals([BLACK], parse("b"))
    }

    @Test
    void red() {
        assertEquals([RED], parse("r"))
    }

    @Test
    void green() {
        assertEquals([GREEN], parse("g"))
    }

    @Test
    void tap() {
        assertEquals([], parse("t"))
        assertEquals([TAP], CostParser.parse("t", true))
    }

    @Test
    void untap() {
        assertEquals([], parse("q"))
        assertEquals([UNTAP], CostParser.parse("q", true))
    }

    @Test
    void addNumericColorless() {
        assertEquals([COLORLESS_6], parse("123"))
    }

    @Test
    void multiXColorless() {
        assertEquals([COLORLESS_X, COLORLESS_X, GREEN], parse("xxg"))
    }

    @Test
    void garbage() {
        assertEquals([WHITE, RED, BLUE, GREEN, COLORLESS_X, BLACK, COLORLESS_6], parse("123qwertyuiopsdfghjklzxcvbnm"))
    }

    @Test
    void fireball() {
        assertEquals([COLORLESS_X, RED], parse("xr"))
    }

    @Test
    void crawWorm() {
        assertEquals([COLORLESS_4, GREEN, GREEN], parse("4gg"))
    }

    @Test
    void chromanticore() {
        assertEquals([WHITE, BLUE, BLACK, RED, GREEN], parse("wubrg"))
    }

    @Test
    void akroanHoplite() {
        assertEquals([RED, WHITE], parse("rw"))
    }

    @Test
    void nicolBolas() {
        assertEquals([COLORLESS_2, BLUE, BLUE, BLACK, BLACK, RED, RED], parse("2uubbrr"))
        // this cost string is deliberately ordered "weird"
        assertEquals([RED, BLUE, BLACK, RED, COLORLESS_2, BLUE, BLACK], parse("RUBR2UB"))
    }

    @Test
    void hybrids() {
        assertEquals([
            new CostType('2', ManaColor.COLORLESS),
            new CostType('2/u', [ManaColor.COLORLESS, ManaColor.BLUE]),
            new CostType('w/g', [ManaColor.WHITE, ManaColor.GREEN]),
            new CostType('g/p', ManaColor.GREEN)
        ], parse("2{2/u}{w/g}{g/p}"))
    }

    @Test
    void parts() {
        assertEquals(['w', 'u', 'b', 'r', 'g'], CostParser.parts("wubrg"))
        assertEquals(['2', '2/u', 'w/g', 'g/p'], CostParser.parts("2{2/u}{w/g}{g/p}"))
    }

}
