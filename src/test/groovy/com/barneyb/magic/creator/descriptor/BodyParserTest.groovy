package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.AbilityText
import com.barneyb.magic.creator.compositor.FlavorText
import com.barneyb.magic.creator.compositor.Paragraph
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class BodyParserTest {

    RenderSet rs

    @Before
    void _makeRenderSet() {
        rs = AssetDescriptor.fromStream(getClass().classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("print")
    }

    @Test
    void simpleSymbols() {
        assertEquals([[
            CostType.GREEN,
            new AbilityText(', '),
            CostType.TAP,
            new AbilityText(': Do something.'),
        ]], BodyParser.parse('{G}, {T}: Do something.'))
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
        ]], BodyParser.parse('{u}, {t}: Do something unless {x}{r} is paid by Johann.'))
    }

    @Test
    void inlineFlavor() {
        assertEquals([[
            new AbilityText('Scry 2. '),
            new FlavorText('(Look at ... in any order.)'),
        ]], BodyParser.parse('Scry 2. {(Look at ... in any order.)}'))
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
            BodyParser.parse('\n\none\ntwo\n\nthree\n\n\n\nfour\n')
        )
    }

    @Test
    void inlineLineBreaks() {
        assertEquals(
            [
                [
                    new AbilityText("flavor "),
                    new FlavorText("with")
                ],
                [new FlavorText("nested")],
                [new Paragraph()],
                [new FlavorText("newlines")],
            ],
            BodyParser.parse("flavor {with\nnested\n\nnewlines}")
        )
    }

}
