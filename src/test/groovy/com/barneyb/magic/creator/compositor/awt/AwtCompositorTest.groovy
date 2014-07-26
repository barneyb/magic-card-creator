package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.Cards
import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
/**
 * This is kind of a BS test, because it doesn't actually assert anything, it
 * just spits out some composed cards, and then a human can decide if they're
 * right or not.  So it's more of just an example runner, but easy to run via
 * jUnit's infrastructure.
 */
@org.junit.Ignore
class AwtCompositorTest {

    static final File outputDir = new File("build")

    @BeforeClass
    static void ensureTarget() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        def f = new File(outputDir, "preview_awt.html")
        f.text = """\
<html>
<body>
<img src="sally.png" />
<img src="counterspell.png" />
<img src="nightmare.png" />
<img src="blitz-hellion.png" />
<img src="barney.png" />
<img src="brothel.png" />
</body>
</html>"""
    }

    RenderSet rs

    @Before
    void _makeRenderSet() {
        rs = AssetDescriptor.fromStream(AwtCompositorTest.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("screen")
    }

    @Test
    void sally() {
        def m = Cards.sally(rs)
        new AwtCompositor().compose(m, rs, new File(outputDir, "sally.png").newOutputStream())
    }

    @Test
    void counterspell() {
        def m = Cards.counterspell(rs)
        new AwtCompositor().compose(m, rs, new File(outputDir, "counterspell.png").newOutputStream())
    }

    @Test
    void nightmare() {
        def m = Cards.nightmare(rs)
        new AwtCompositor().compose(m, rs, new File(outputDir, "nightmare.png").newOutputStream())
    }

    @Test
    void barney() {
        def m = Cards.barney(rs)
        new AwtCompositor().compose(m, rs, new File(outputDir, "barney.png").newOutputStream())
    }

    @Test
    void blitz_hellion() {
        def m = Cards.hellion(rs)
        new AwtCompositor().compose(m, rs, new File(outputDir, "blitz-hellion.png").newOutputStream())
    }

    @Test
    void brothel() {
        def m = Cards.brothel(rs)
        new AwtCompositor().compose(m, rs, new File(outputDir, "brothel.png").newOutputStream())
    }

}
