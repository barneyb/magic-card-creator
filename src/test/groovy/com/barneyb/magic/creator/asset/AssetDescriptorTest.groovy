package com.barneyb.magic.creator.asset

import org.junit.Test

import java.awt.*

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class AssetDescriptorTest {

    @Test
    void loading() {
        def d = AssetDescriptor.fromStream(AssetDescriptorTest.classLoader.getResourceAsStream('assets/test-descriptor.json'))
        assertEquals(['screen'] as Set, d.renderSets.keySet())
        def rs = d.renderSets.values().first()

        assertEquals('screen', rs.key)

        assertEquals(rs, rs.frames.renderSet)
        assertEquals('frames', rs.frames.key)

        assertEquals(new Dimension(400, 560), rs.frames.size)
        assertEquals('png', rs.frames.type)

        assertEquals(new TextBox(36, 36, 328, 22, "Goudy Old Style", true), rs.titlebar)

        assertEquals(new Rectangle(36, 69, 328, 242), rs.artwork)

        assertEquals(new TextBox(38, 352, 324, 150, "Garamond", false), rs.textbox)
    }

}
