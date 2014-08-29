package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.core.DefaultLineBreak
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

/**
 *
 *
 * @author barneyb
 */
@Mixin(TestMixin)
class TextParserTest {

    TextParser parser

    @Before
    void _makeParser() {
        parser = new TextParser()
    }

    @Test
    void toLines() {
        // the \u0020 escapes are simple spaces, but encoded so that IntelliJ
        // won't strip them from the ends of lines.
        assertEquals([
            "alex",
            "",
            "fred",
            "jim",
            "  sally",
            "johan",
            "",
            "tom",
            "will"
        ], parser.toLines("""
\u0020
        alex
\u0020\u0020\u0020\u0020
        fred\u0020\u0020
        jim
          sally\u0020
        johan

        tom
        will

"""))
    }

    @Test
    void toParagraphs() {
        // the \u0020 escapes are simple spaces, but encoded so that IntelliJ
        // won't strip them from the ends of lines.
        assertEquals([
            "alex",
            "fred jim\n  sally\njohan",
            "tom will"
        ], parser.toParagraphs([
            "alex",
            "",
            "fred",
            "jim",
            "  sally",
            "johan",
            "",
            "tom",
            "will"
        ]))
    }

    @Test
    void simpleSymbols() {
        assertEquals([[
              sg('g'),
              rt(', '),
              sg('t'),
              rt(': Do something.'),
          ]], parser.getRulesText('{G}, {T}: Do something.'))
    }

    @Test
    void complexSymbols() {
        assertEquals([[
              sg('u'),
              rt(', '),
              sg('t'),
              rt(': Do something unless '),
              sg('x', 'b/r'),
              rt(' is paid by Johann.'),
          ]], parser.getRulesText('{u}, {t}: Do something unless {x}{b/r} is paid by Johann.'))
    }

    @Test
    void a_breaks() {
        def text = '\n\none\ntwo\n\nthree\n\n\n\nfour\n five\n'
        assertEquals([
            "one",
            "two",
            "",
            "three",
            "",
            "four",
            " five"
        ], parser.toLines(text))
        assertEquals([
            "one two",
            "three",
            "four\n five"
        ], parser.toParagraphs(parser.toLines(text)))
        assertEquals(
            [
                [rt("one two")],
                [rt("three")],
                [rt("four"), new DefaultLineBreak(), rt(" five")],
            ],
            parser.getRulesText(text)
        )
    }

}
