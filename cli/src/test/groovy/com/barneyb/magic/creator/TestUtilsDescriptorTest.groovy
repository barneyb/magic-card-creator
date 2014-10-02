package com.barneyb.magic.creator
import com.barneyb.magic.creator.descriptor.XmlCardSetReader
import com.barneyb.magic.creator.descriptor.markdown.MarkdownCardSetReader
import org.junit.Test

import static com.barneyb.magic.creator.util.Assert.*

class TestUtilsDescriptorTest {

    @Test
    void testUtilsDescriptors() {
        // this is kind of a BS test, but whatever.  it's more like sanity.
        def xml = new XmlCardSetReader(getClass().classLoader.getResource('test-set.xml'))
        def md = new MarkdownCardSetReader(getClass().classLoader.getResource('test-set.md'))
        assertCardSet(xml.read(), md.read())
    }

}
