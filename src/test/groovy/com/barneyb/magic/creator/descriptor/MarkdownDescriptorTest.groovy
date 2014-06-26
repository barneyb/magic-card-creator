package com.barneyb.magic.creator.descriptor

import org.junit.Test

import static com.barneyb.magic.creator.Assert.assertCard
import static com.barneyb.magic.creator.Assert.assertCardSet
import static com.barneyb.magic.creator.Cards.*
import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class MarkdownDescriptorTest {

    public static final URL TEST_SET_DESCRIPTOR = MarkdownDescriptorTest.classLoader.getResource("test_set.md")

    protected def desc(String src) {
        new MarkdownDescriptor(TEST_SET_DESCRIPTOR, new StringReader(src))
    }

    @Test
    void setAttributes() {
        def cs = desc("""
Test Set
===
one
two

three

## Sally 1R

Creature - Human 1/1

*Sally eats souls.*
""").cardSet
        assertEquals("Test Set", cs.name)
        assertEquals("one two three", cs.footer)
    }

    @Test
    void card() {
        def cs = desc("""
## Sally 1R

![Sally Mann](artwork/sally.jpg)

Creature - Human 1/1

{U}{T}: Tap target creature and pay 1 life.

*Sally doesn't like you.*

*Or you.*
""").cardSet
        assertEquals(1, cs.size())
        assertCard(sally(), cs.first())
    }

    @Test
    void testSet() {
        assertCardSet(set(), new MarkdownDescriptor(TEST_SET_DESCRIPTOR).cardSet)
    }

    @Test
    void constructor_equivalence() {
        def cl = getClass().classLoader
        def path = "test_set.md"
        def res = cl.getResource(path)
        def url = new MarkdownDescriptor(res)
        def stream = new MarkdownDescriptor(res, res.newInputStream())
        def string = new MarkdownDescriptor(res, res.text)
        def reader = new MarkdownDescriptor(res, res.newReader())
        assertEquals(url.cardSet, stream.cardSet)
        assertEquals(url.cardSet, string.cardSet)
        assertEquals(url.cardSet, reader.cardSet)
    }

}
