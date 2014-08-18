package com.barneyb.magic.creator.descriptor
import org.junit.Test

import static com.barneyb.magic.creator.descriptor.CostType.*
import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class CostTypeOrderingTest {

    protected void check(List<CostType> colors) {
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
