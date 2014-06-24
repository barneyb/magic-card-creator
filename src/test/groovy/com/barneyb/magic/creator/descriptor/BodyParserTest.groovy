package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.asset.Descriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Paragraph
import com.barneyb.magic.creator.compositor.RenderableString
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
            new RenderableString(', ', false),
            CostType.TAP,
            new RenderableString(': Do something.', false),
        ]], BodyParser.parseAbilities('{G}, {T}: Do something.'))
    }

    @Test
    void complexSymbols() {
        assertEquals([[
            CostType.BLUE,
            new RenderableString(', ', false),
            CostType.TAP,
            new RenderableString(': Do something unless ', false),
            CostType.COLORLESS_X,
            CostType.RED,
            new RenderableString(' is paid by Johann.', false),
        ]], BodyParser.parseAbilities('{u}, {t}: Do something unless {x}{r} is paid by Johann.'))
    }

    @Test
    void inlineFlavor() {
        assertEquals([[
            new RenderableString('Scry 2. ', false),
            new RenderableString('(Look at ... in any order.)', true),
        ]], BodyParser.parseAbilities('Scry 2. {(Look at ... in any order.)}'))
    }

    @Test
    void a_breaks() {
        assertEquals(
            [
                [new RenderableString("one", false)],
                [new RenderableString("two", false)],
                [new Paragraph()],
                [new RenderableString("three", false)],
                [new Paragraph()],
                [new RenderableString("four", false)],
            ],
            BodyParser.parseAbilities('\n\none\ntwo\n\nthree\n\n\n\nfour\n')
        )
    }

    @Test
    void f_breaks() {
        assertEquals(
            [
                [new RenderableString("one", true)],
                [new RenderableString("two", true)],
                [new Paragraph()],
                [new RenderableString("three", true)],
                [new Paragraph()],
                [new RenderableString("four", true)],
            ],
            BodyParser.parseFlavor('\n\none\ntwo\n\nthree\n\n\n\nfour\n')
        )
    }

}
