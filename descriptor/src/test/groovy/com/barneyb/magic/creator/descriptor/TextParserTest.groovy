package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.descriptor.schema.NonNormativeTextType
import com.barneyb.magic.creator.descriptor.schema.RulesTextType
import org.junit.Before
import org.junit.Test

import javax.xml.bind.JAXBElement
import javax.xml.namespace.QName

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
          ]], parser.getText('{G}, {T}: Do something.', DefaultRulesText))
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
          ]], parser.getText('{u}, {t}: Do something unless {x}{b/r} is paid by Johann.', DefaultRulesText))
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
            parser.getText(text, DefaultRulesText)
        )
    }

    @Test
    void complexRules() {
        def el = new RulesTextType()
        el.content.addAll([
            "ability",
            new JAXBElement(new QName("reminder"), NonNormativeTextType, new NonNormativeTextType(content: [
                "multi-line",
                new JAXBElement(new QName("br"), String, null),
                "\nreminder"
            ])),
        ])
        assertEquals([[
            rt("ability"),
            nnt("multi-line"),
            lb(),
            nnt("reminder")
        ]], parser.getRulesText(el))
    }

    @Test
    void complexFlavor() {
        def el = new NonNormativeTextType()
        el.content.addAll([
            "flavor",
            new JAXBElement<String>(new QName("br"), String, null),
            "\nauthor",
        ])
        assertEquals([[
            nnt("flavor"),
            lb(),
            nnt("author")
        ]], parser.getNonNormativeText(el))
    }

}
