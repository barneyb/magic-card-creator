package com.barneyb.magic.creator.core

import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 *
 *
 * @author barneyb
 */
class ClasspathRasterImageTest {

    @Test
    void toString_() {
        assertEquals("{classpath:red.jpg}", new ClasspathRasterImage("red.jpg").toString())
    }

}
