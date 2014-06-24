package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.asset.Descriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.AbilityText
import com.barneyb.magic.creator.compositor.FlavorText
import com.barneyb.magic.creator.compositor.Paragraph
import com.barneyb.magic.creator.compositor.awt.AwtCompositorTest
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class BodyParserTest {

    RenderSet rs

    @Before
    void _makeRenderSet() {
        rs = Descriptor.fromStream(AwtCompositorTest.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("screen")
    }

    @Test
    void simpleSymbols() {
        assertEquals([[
            CostType.GREEN,
            new AbilityText(', '),
            CostType.TAP,
            new AbilityText(': Do something.'),
        ]], BodyParser.parseAbilities('{G}, {T}: Do something.'))
    }

    @Test
    void complexSymbols() {
        assertEquals([[
            CostType.BLUE,
            new AbilityText(', '),
            CostType.TAP,
            new AbilityText(': Do something unless '),
            CostType.COLORLESS_X,
            CostType.RED,
            new AbilityText(' is paid by Johann.'),
        ]], BodyParser.parseAbilities('{u}, {t}: Do something unless {x}{r} is paid by Johann.'))
    }

    @Test
    void inlineFlavor() {
        assertEquals([[
            new AbilityText('Scry 2. '),
            new FlavorText('(Look at ... in any order.)'),
        ]], BodyParser.parseAbilities('Scry 2. {(Look at ... in any order.)}'))
    }

    @Test
    void badFlavor() {
        assertEquals([[
            new AbilityText('unclosed '),
            new FlavorText('flavor'),
        ]], BodyParser.parse('unclosed {flavor'))
    }

    @Test
    void a_breaks() {
        assertEquals(
            [
                [new AbilityText("one")],
                [new AbilityText("two")],
                [new Paragraph()],
                [new AbilityText("three")],
                [new Paragraph()],
                [new AbilityText("four")],
            ],
            BodyParser.parseAbilities('\n\none\ntwo\n\nthree\n\n\n\nfour\n')
        )
    }

    @Test
    void f_breaks() {
        assertEquals(
            [
                [new FlavorText("one")],
                [new FlavorText("two")],
                [new Paragraph()],
                [new FlavorText("three")],
                [new Paragraph()],
                [new FlavorText("four")],
            ],
            BodyParser.parseFlavor('\n\none\ntwo\n\nthree\n\n\n\nfour\n')
        )
    }

}
