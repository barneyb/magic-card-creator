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
            BodyParser.parseAbilities("""

one
two

three



four""")
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
            BodyParser.parseFlavor("""

one
two

three



four""")
        )
    }

}
