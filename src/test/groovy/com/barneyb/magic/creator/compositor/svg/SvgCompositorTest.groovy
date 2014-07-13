package com.barneyb.magic.creator.compositor.svg
import com.barneyb.magic.creator.Cards
import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
/**
 *
 * @author bboisvert
 */
class SvgCompositorTest {

    static final File outputDir = new File("build")

    @BeforeClass
    static void ensureTarget() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        def f = new File(outputDir, "preview_svg.html")
        f.text = """\
<html>
<head>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
</head>
<body>
<embed src="sally.svg" />
<embed src="counterspell.svg" />
<embed src="nightmare.svg" />
<embed src="blitz-hellion.svg" />
<embed src="barney.svg" />
<embed src="brothel.svg" />
</body>
</html>"""
    }

    RenderSet rs

    @Before
    void _makeRenderSet() {
        rs = AssetDescriptor.fromStream(SvgCompositorTest.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("print")
    }

    @Test
    void sally() {
        def m = Cards.sally(rs)
        new SvgCompositor().compose(m, rs, new File(outputDir, "sally.svg").newOutputStream())
    }

    @Test
    void counterspell() {
        def m = Cards.counterspell(rs)
        new SvgCompositor().compose(m, rs, new File(outputDir, "counterspell.svg").newOutputStream())
    }

    @Test
    void nightmare() {
        def m = Cards.nightmare(rs)
        new SvgCompositor().compose(m, rs, new File(outputDir, "nightmare.svg").newOutputStream())
    }

    @Test
    void barney() {
        def m = Cards.barney(rs)
        new SvgCompositor().compose(m, rs, new File(outputDir, "barney.svg").newOutputStream())
    }

    @Test
    void blitz_hellion() {
        def m = Cards.hellion(rs)
        new SvgCompositor().compose(m, rs, new File(outputDir, "blitz-hellion.svg").newOutputStream())
    }

    @Test
    void brothel() {
        def m = Cards.brothel(rs)
        new SvgCompositor().compose(m, rs, new File(outputDir, "brothel.svg").newOutputStream())
    }

}
