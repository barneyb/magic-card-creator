package com.barneyb.magic.creator.compositor
import org.junit.Test

import java.text.BreakIterator
import java.text.StringCharacterIterator

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class MagicBreakIteratorProviderTest {

    protected def getSections(String s) {
        def itr = MagicBreakIteratorProvider.getLineInstance()
        itr.text = new StringCharacterIterator(s)
        int i, l = 0
        List<String> sections = []
        while ((i = itr.next()) != BreakIterator.DONE) {
            sections << s.substring(l, i)
            l = i
        }
        sections
    }

    @Test
    void plusOneTwelve() {
        assertEquals(
            ["creatures ", "get ", "+1/+12 ", "until ", "end"],
            getSections("creatures get +1/+12 until end")
        )
    }

    @Test
    void reference() {
        assertEquals(
            ["Enchanted ", "creature ", "becomes ", "a ", "0/2 ", "lunatic"],
            getSections("Enchanted creature becomes a 0/2 lunatic")
        )
    }

    @Test
    void minusTwelveOne() {
        assertEquals(
            ["creatures ", "get ", "-12/-1 ", "until ", "end"],
            getSections("creatures get -12/-1 until end")
        )
    }

    @Test
    void preceding() {
        def s = "creatures get +1/+12 until end"
        def itr = MagicBreakIteratorProvider.getLineInstance()
        itr.text = new StringCharacterIterator(s)
        def i = s.indexOf('+') + 1
        def e = s.indexOf(' ', i)
        (i..<e).each {
            assertEquals("preceding $it", i - 1, itr.preceding(it))
        }
    }

}
