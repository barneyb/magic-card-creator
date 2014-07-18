package com.barneyb.magic.creator.compositor

import org.junit.Before
import org.junit.Test

import java.awt.font.TextAttribute

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
@Ignore
class LayoutUtilsTest {

    LayoutUtils utils

    @Before
    void _makeUtils() {
        utils = new LayoutUtils()
    }

    @Test
    void simpleLine() {
        assertEquals(new LineLayout(61.25f, 0, 38.77125f, 1), utils.line(new Dimension2D(717, 49), "Emery", [
            (TextAttribute.FAMILY): "Matrix",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ]))
    }

    @Test
    void wideLine() {
        assertEquals(new LineLayout(61.25f, 0, 38.77125f, 0.6473984f), utils.line(new Dimension2D(435.510635376, 49), "Barney of the Green Woods", [
            (TextAttribute.FAMILY): "Matrix",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ]))
    }

    @Test
    void centeredLine() {
        assertEquals(new LineLayout(39.75841f, 22.202332f, 35.56514f, 1), utils.line(new Dimension2D(120, 45), "1 / 1", [
            (TextAttribute.FAMILY): "Goudy Old Style",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ], Align.CENTER))
    }

    @Test
    void centeredWideLine() {
        assertEquals(new LineLayout(39.75841f, 0, 35.56514f, 0.88735765f), utils.line(new Dimension2D(120, 45), "100/100", [
            (TextAttribute.FAMILY): "Goudy Old Style",
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ], Align.CENTER))
    }

}
