package com.barneyb.magic.creator.descriptor
import org.junit.Test

import static com.barneyb.magic.creator.descriptor.FrameBaseType.*
import static com.barneyb.magic.creator.descriptor.FrameModifier.*
import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class FrameBaseTypeTest {

    @Test
    void keys() {
        assertEquals("artifact", ARTIFACT.assetKey)
        assertEquals("black", BLACK.assetKey)
    }

    @Test
    void plus() {
        assertEquals(new CompoundFrame(ARTIFACT, [Type.CREATURE]), ARTIFACT + Type.CREATURE)
    }

    @Test
    void minus() {
        assertEquals(ARTIFACT, ARTIFACT - Type.CREATURE)
    }

}
