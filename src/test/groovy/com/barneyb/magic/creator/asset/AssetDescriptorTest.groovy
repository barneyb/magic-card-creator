package com.barneyb.magic.creator.asset

import org.junit.Test

import java.awt.Dimension
import java.awt.Rectangle

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

        assertEquals(new Rectangle(36, 69, 328, 242), rs.artwork)

    }

}
