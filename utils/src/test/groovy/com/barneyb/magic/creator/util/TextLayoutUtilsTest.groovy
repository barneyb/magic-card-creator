package com.barneyb.magic.creator.util
import org.junit.Before
import org.junit.Test

import java.awt.Dimension
import java.awt.font.TextAttribute

import static org.junit.Assert.*
/**
 *
 *
 * @author barneyb
 */
class TextLayoutUtilsTest {

    TextLayoutUtils utils

    @Before
    void _makeUtils() {
        utils = new TextLayoutUtils()
    }

    @Test
    void simpleLine() {
        utils.line(new Dimension(717, 49), "Emery", [
            (TextAttribute.FAMILY): "Matrix",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ])
    }

    @Test
    void wideLine() {
        def r = utils.line(new Dimension(435, 49), "Barney of the Green Woods", [
            (TextAttribute.FAMILY): "Matrix",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ])
        assertTrue(r.scaled)
    }

    @Test
    void centeredLine() {
        def dim = new Dimension(120, 45)
        def text = "1/1"
        def attrs = [
            (TextAttribute.FAMILY): "Goudy Old Style",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ]
        def centered = utils.line(dim, text, attrs, Align.CENTER)
        def leading = utils.line(dim, text, attrs)
        assertTrue(Math.abs(centered.x - leading.x) >  0.0000001)
        assertEquals(centered.y, leading.y, 0.0000001)
        assertEquals(centered.scale, leading.scale, 0.0000001)
    }

    @Test
    void centeredWideLine() {
        def dim = new Dimension(120, 45)
        def text = "100/100"
        def attrs = [
            (TextAttribute.FAMILY): "Goudy Old Style",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ]
        def centered = utils.line(dim, text, attrs, Align.CENTER)
        def leading = utils.line(dim, text, attrs)
        assertEquals(centered.x, leading.x, 0.0000001)
        assertEquals(centered.y, leading.y, 0.0000001)
        assertEquals(centered.scale, leading.scale, 0.0000001)
    }

}
