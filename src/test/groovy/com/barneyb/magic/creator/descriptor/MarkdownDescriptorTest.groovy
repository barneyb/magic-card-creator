package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.Cards
import org.junit.Test

import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class MarkdownDescriptorTest {

    protected assertCard(Card e, Card a) {
        try {
            assertEquals(e.title, a.title)
            assertEquals(e.costString, a.costString)
            assertEquals(e.artwork, a.artwork)
            assertEquals(e.type, a.type)
            assertEquals(e.subtype, a.subtype)
            assertEquals(e.body, a.body)
            assertEquals(e.power, a.power)
            assertEquals(e.toughness, a.toughness)
            assertEquals(e.artist, a.artist)
        } catch (AssertionError ae) {
            println "expected: $e"
            println "  actual: $a"
            throw ae
        }
    }

    @Test
    void setAttributes() {
        def cs = new MarkdownDescriptor("""
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
        def cs = new MarkdownDescriptor("""
## Sally 1R

![Sally Mann](artwork/sally.jpg)

Creature - Human 1/1

{U}{T}: Tap target creature and pay 1 life.

*Sally doesn't like you.*

*Or you.*
""").cardSet
        assertEquals(1, cs.size())
        assertCard(Cards.sally(), cs.first())
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
