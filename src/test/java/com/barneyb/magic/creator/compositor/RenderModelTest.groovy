package com.barneyb.magic.creator.compositor

import org.junit.Test

import static junit.framework.Assert.*

/**
 *
 * @author bboisvert
 */
class RenderModelTest {

    @Test
    void showPT() {
        def m = new RenderModel(powerToughness: null)
        assertFalse(m.powerToughnessVisible)

        m.powerToughness = "1/2"
        assertTrue(m.powerToughnessVisible)
    }

}
