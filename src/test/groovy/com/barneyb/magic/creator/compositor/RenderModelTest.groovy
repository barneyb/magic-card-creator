package com.barneyb.magic.creator.compositor

import com.barneyb.magic.creator.Cards
import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.awt.AwtCompositorTest
import org.junit.Before
import org.junit.Test

import static com.barneyb.magic.creator.Assert.*
import static junit.framework.Assert.*
/**
 *
 * @author bboisvert
 */
class RenderModelTest {

    RenderSet rs

    @Before
    void _makeRenderSet() {
        rs = AssetDescriptor.fromStream(AwtCompositorTest.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet("screen")
    }

    @Test
    void showPT() {
        def m = new RenderModel(powerToughness: null)
        assertFalse(m.powerToughnessVisible)

        m.powerToughness = "1/2"
        assertTrue(m.powerToughnessVisible)
    }

    @Test
    void fromCard_sally() {
        assertRenderModel(
            Cards.sally(rs),
            RenderModel.fromCard(Cards.sally(), rs)
        )
    }

    @Test
    void fromCard_counterspell() {
        assertRenderModel(
            Cards.counterspell(rs),
            RenderModel.fromCard(Cards.counterspell(), rs)
        )
    }

    @Test
    void fromCard_nightmare() {
        assertRenderModel(
            Cards.nightmare(rs),
            RenderModel.fromCard(Cards.nightmare(), rs)
        )
    }

    @Test
    void fromCard_hellion() {
        assertRenderModel(
            Cards.hellion(rs),
            RenderModel.fromCard(Cards.hellion(), rs)
        )
    }

    @Test
    void fromCard_barney() {
        assertRenderModel(
            Cards.barney(rs),
            RenderModel.fromCard(Cards.barney(), rs)
        )
    }

}
