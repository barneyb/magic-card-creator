package com.barneyb.magic.creator.core

import org.junit.Test

import java.awt.*

import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertTrue

/**
 *
 *
 * @author barneyb
 */
class UrlRasterImageTest {

    @Test
    void loadItUp() {
        def ri = new UrlRasterImage(getClass().classLoader.getResource("red.jpg"))
        assertTrue(ri.exists)
        assertEquals(new Dimension(48, 48), ri.size)
    }

}
