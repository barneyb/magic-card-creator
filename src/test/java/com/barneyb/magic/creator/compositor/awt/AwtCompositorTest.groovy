package com.barneyb.magic.creator.compositor.awt
import com.barneyb.magic.creator.asset.Descriptor
import com.barneyb.magic.creator.asset.RemoteImage
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.RenderableString
import com.barneyb.magic.creator.descriptor.CostType
import com.barneyb.magic.creator.descriptor.FrameType
import org.junit.Test
/**
 * This is kind of a BS test, because it doesn't actually assert anything, it
 * just spits out some composed cards, and then a human can decide if they're
 * right or not.  So it's more of just an example runner, but easy to run via
 * jUnit's infrastructure.
 */
class AwtCompositorTest {

    @Test
    void sally() {
        def rs = Descriptor.fromStream(AwtCompositorTest.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("screen")
        def m = new RenderModel(
            frame: rs.frames.getImageAsset(FrameType.RED_CREATURE),
            title: "Sally",
            cost: [CostType.COLORLESS_1, CostType.RED].collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(new URL("http://blogs.cornell.edu/art3606bhs74/files/2013/10/Sally-Mann-Black-Eye-1991-painting-artwork-print-1lpzmeq.jpg")),
            type: "Creature \u2014 Human",
            body: [
                [
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.TAP),
                    new RenderableString(": tap target creature and pay 1 life.", false),
                ],
                [
                    new RenderableString("Sally doesn't like you.", true)
                ]
            ],
            powerToughness: "1/1",
            artist: "Sally Mann",
            footer: "\u00A9 2014 Barney Boisvert (1/5)"
        )
        new AwtCompositor().compose(m, rs, new File("sally.png").newOutputStream())
    }

}
