package com.barneyb.magic.creator.textlayout

import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.icon.DefaultIconFactory
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import com.barneyb.magic.creator.util.XmlUtils
import org.apache.batik.svggen.SVGGraphics2D
import org.junit.Before
import org.junit.Test

import java.awt.*
import java.awt.font.TextAttribute

import static org.junit.Assert.*

/**
 * I test some of the layout stuff, but MUST do it in a somewhat careful way,
 * specifically avoiding any assertions which depend on the presence of
 * specific fonts or other external system resources.  Unlike most tests, the
 * tests in this class are not designed to guarantee specific behaviour, but
 * instead to guarantee that classes of behaviour are functional and correct
 * in relation to one another.
 *
 * @author barneyb
 */
class LayoutUtilsTest {

    LayoutUtils utils

    SymbolFactory sf = new DefaultSymbolFactory()
    SymbolIconFactory sif = new DefaultIconFactory()

    @Before
    void _makeUtils() {
        utils = new LayoutUtils()
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
        assertEquals(centered.fontSize, leading.fontSize, 0.0000001)
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
        assertEquals(centered.fontSize, leading.fontSize, 0.0000001)
        assertEquals(centered.scale, leading.scale, 0.0000001)
    }

    @Test
    void block() {

        // todo: this doesn't work...

        utils.block(new SVGGraphics2D(XmlUtils.create()), new Rectangle(0, 0, 500, 500), [
            [new DefaultRulesText("Double strike")],
            [sif.getIcons(sf.getCost('ub')), new DefaultRulesText(', '), sif.getIcons(sf.getCost('t')), new DefaultRulesText(': tap up to two target creatures.')],
            [new DefaultNonNormativeText('The knights are fast, but their trickery means they are rarely forced to use their speed.')]
        ], [:], [:], { Object... stuff -> })
    }

}
