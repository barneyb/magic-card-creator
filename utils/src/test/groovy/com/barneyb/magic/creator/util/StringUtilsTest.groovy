package com.barneyb.magic.creator.util

import org.junit.Test

import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class StringUtilsTest {

    @Test
    void decodeEscapes() {
        assertEquals('simple', StringUtils.decodeEscapes("simple"))
        assertEquals('tab\tdelimited', StringUtils.decodeEscapes("tab\\tdelimited"))
        assertEquals('a \u00b6 (para)', StringUtils.decodeEscapes("a \\u00b6 (para)"))
        assertEquals('a \u00a9', StringUtils.decodeEscapes("a \\u00A9"))
        assertEquals('a \u00a9 \u00b6', StringUtils.decodeEscapes("a \\u00A9 \\u00B6"))
        assertEquals('a \\\\u00A9', StringUtils.decodeEscapes("a \\\\u00A9"))
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
        ], StringUtils.toLines("""
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
        assertEquals([
            "alex",
            "fred jim\n  sally\njohan",
            "tom will"
        ], StringUtils.toParagraphs([
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
    void a_breaks() {
        def text = '\n\none\ntwo\n\nthree\n\n\n\nfour\n five\n'
        def lines = StringUtils.toLines(text)
        def paragraphs = StringUtils.toParagraphs(lines)

        assertEquals([
            "one",
            "two",
            "",
            "three",
            "",
            "four",
            " five"
        ], lines)

        assertEquals([
            "one two",
            "three",
            "four\n five"
        ], paragraphs)
    }

    static class StreamVisitor implements StringUtils.ParseVisitor {

        List stream = []

        @Override
        void startParagraph() {
            stream << "<p>"
        }

        @Override
        void text(String text) {
            stream << text
        }

        @Override
        void lineBreak() {
            stream << "<br>"
        }

        @Override
        void symbols(List<String> keys) {
            stream << keys.collect { "{$it}" }.join("")
        }

        @Override
        void endParagraph() {
            stream << "</p>"
        }
    }

    @Test
    void simpleSymbols() {
        def v = new StreamVisitor()
        StringUtils.parseTextAndSymbols('{G}, {T}: Do something.', v)
        assertEquals([
            '<p>',
            '{G}',
            ', ',
            '{T}',
            ': Do something.',
            '</p>'
        ], v.stream)
    }

    @Test
    void complexSymbols() {
        def v = new StreamVisitor()
        StringUtils.parseTextAndSymbols('{u}, {t}: Do something unless {x}{b/r} is\n paid by Johann.\n\n{t}: Target opponent must sing the alphabet song.', v)
        assertEquals([
            '<p>',
            '{u}',
            ', ',
            '{t}',
            ': Do something unless ',
            '{x}{b/r}',
            ' is',
            '<br>',
            ' paid by Johann.',
            '</p>',
            '<p>',
            '{t}',
            ': Target opponent must sing the alphabet song.',
            '</p>',
        ], v.stream)
    }
    
}
