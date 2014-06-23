package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.ClasspathImage
import com.barneyb.magic.creator.asset.Descriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.RenderableString
import com.barneyb.magic.creator.descriptor.CostType
import com.barneyb.magic.creator.descriptor.FrameBaseType
import com.barneyb.magic.creator.descriptor.FrameModifier
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
/**
 * This is kind of a BS test, because it doesn't actually assert anything, it
 * just spits out some composed cards, and then a human can decide if they're
 * right or not.  So it's more of just an example runner, but easy to run via
 * jUnit's infrastructure.
 */
class AwtCompositorTest {

    static final File outputDir = new File("build")

    @BeforeClass
    static void ensureTarget() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        def f = new File(outputDir, "preview.html")
        f.text = """\
<html>
<body>
<img src="sally.png" />
<img src="nightmare.png" />
<img src="blitz-hellion.png" />
<img src="barney.png" />
</body>
</html>"""
    }

    RenderSet rs

    @Before
    void _makeRenderSet() {
        rs = Descriptor.fromStream(AwtCompositorTest.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("screen")
    }

    @Test
    void nightmare() {
        def m = new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.BLACK + FrameModifier.Type.CREATURE),
            title: "Nightmare",
            cost: [CostType.COLORLESS_5, CostType.BLACK].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/nightmare.jpg"),
            type: "Creature \u2013 Nightmare Horse",
            body: [
                [
                    new RenderableString("Flying", false),
                ],
                [
                    new RenderableString("Nightmare's power and toughness are each equal to the number of Swamps you control.", false),
                ],
                [
                    new RenderableString("The thunder of its hooves beats dreams into despair.", true),
                ]
            ],
            powerToughness: "*/*",
            whiteFooterText: true,
            artist: "catfish08",
            footer: "\u00A9 2014 Barney Boisvert (3/5)"
        )
        new AwtCompositor().compose(m, rs, new File(outputDir, "nightmare.png").newOutputStream())
    }

    @Test
    void sally() {
        def m = new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.RED + FrameModifier.Type.CREATURE),
            title: "Sally",
            cost: [CostType.COLORLESS_1, CostType.RED].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/sally.jpg"),
            type: "Creature \u2013 Human",
            body: [
                [
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.TAP),
                    new RenderableString(": Tap target creature and pay 1 life.", false),
                ],
                [
                    new RenderableString("Sally doesn't like you.", true)
                ],
                [
                    new RenderableString("Or you.", true)
                ]
            ],
            powerToughness: "1/1",
            artist: "Sally Mann",
            footer: "\u00A9 2014 Barney Boisvert (1/5)"
        )
        new AwtCompositor().compose(m, rs, new File(outputDir, "sally.png").newOutputStream())
    }

    @Test
    void barney() {
        def m = new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.GOLD + FrameModifier.Type.ENCHANTMENT_CREATURE),
            title: "Barney of the Green Woods",
            cost: [CostType.COLORLESS_2, CostType.WHITE, CostType.BLUE, CostType.BLACK, CostType.RED, CostType.GREEN].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/barney.jpg"),
            type: "Legendary Enchantment Creature \u2013 Human Legend",
            body: [
                [
                    new RenderableString("Indestructable, hexproof", false),
                ],
                [
                    rs.small.getImageAsset(CostType.COLORLESS_1),
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.GREEN),
                    rs.small.getImageAsset(CostType.TAP),
                    new RenderableString(": Every opponent dies in a fire unless all pay ", false),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.BLACK),
                    rs.small.getImageAsset(CostType.GREEN),
                    new RenderableString(" or ", false),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.RED),
                    rs.small.getImageAsset(CostType.BLACK),
                    rs.small.getImageAsset(CostType.WHITE),
                    new RenderableString(" or ", false),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.BLACK),
                    new RenderableString(" where X equals your life total.", false),
                ],
                [
                    new RenderableString("For the first time in his life, Barney felt warm.  And hungry.", true)
                ]
            ],
            powerToughness: "100/100",
            artist: "Dolores Boisvert",
            footer: "\u00A9 2014 Fucking Barney! (5/5)"
        )
        new AwtCompositor().compose(m, rs, new File(outputDir, "barney.png").newOutputStream())
    }

    @Test
    void blitz_hellion() {
        def m = new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.GOLD + FrameModifier.Type.CREATURE + FrameModifier.Dual.RED_GREEN),
            title: "Blitz Hellion",
            cost: [CostType.COLORLESS_2, CostType.RED, CostType.GREEN].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/hellion.jpg"),
            type: "Creature \u2013 Hellion",
            body: [
                [
                    new RenderableString("Trample, haste", false),
                ],
                [
                    new RenderableString("At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library.", false),
                ],
                [
                    new RenderableString("Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate.", true)
                ]
            ],
            powerToughness: "7/7",
            artist: "Anthony S. Waters",
            footer: "\u00A9 2014 Fucking Barney! (4/5)"
        )
        new AwtCompositor().compose(m, rs, new File(outputDir, "blitz-hellion.png").newOutputStream())
    }

}
