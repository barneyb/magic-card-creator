package com.barneyb.magic.creator.util

import org.junit.Test

import java.awt.*

import static com.barneyb.magic.creator.util.ColorUtils.fromHex
import static com.barneyb.magic.creator.util.ColorUtils.toHex
import static junit.framework.Assert.assertEquals

/**
 *
 *
 * @author barneyb
 */
class ColorUtilsTest {
    
    @Test
    void hexFormats() {
        def ex = new Color(238, 238, 238)
        assertEquals(ex, fromHex("0xEEEEEE"))
        assertEquals(ex, fromHex("#EEEEEE"))
        assertEquals(ex, fromHex("EEEEEE"))
        assertEquals(ex, fromHex("0xeeeeee"))
        assertEquals(ex, fromHex("#eeeeee"))
        assertEquals(ex, fromHex("eeeeee"))
    }

    @Test
    void output() {
        assertEquals("#eeeeee", toHex(new Color(238, 238, 238)))
    }

}
