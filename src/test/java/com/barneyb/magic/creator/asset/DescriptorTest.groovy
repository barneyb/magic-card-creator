package com.barneyb.magic.creator.asset

import org.junit.Test

import java.awt.Dimension
import java.awt.Rectangle

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class DescriptorTest {

    @Test
    void loading() {
        def d = Descriptor.fromStream(DescriptorTest.classLoader.getResourceAsStream('test-descriptor.json'))
        assertEquals(['screen'] as Set, d.renderSets.keySet())
        def rs = d.renderSets.values().first()

        assertEquals(new Dimension(400, 560), rs.frame.size)
        assertEquals('png', rs.frame.type)

        assertEquals(new Rectangle(36, 69, 328, 242), rs.artwork)

    }

}
