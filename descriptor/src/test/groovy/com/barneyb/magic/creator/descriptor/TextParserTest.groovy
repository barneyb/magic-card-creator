package com.barneyb.magic.creator.descriptor

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
    void simpleSymbols() {
        def text = '{G}, {T}: Do something.'
        def body = [
            sg('g'),
            rt(', '),
            sg('t'),
            rt(': Do something.'),
        ]
        assertEquals(body, parser.parse(text, DefaultRulesText))
        assertEquals(text, parser.unparse(body, RulesTextType).content.join(''))
    }

    @Test
    void complexSymbols() {
        def text = '{U}, {T}: Do something unless {X}{B/R} is paid by Johann.'
        def body = [
            sg('u'),
            rt(', '),
            sg('t'),
            rt(': Do something unless '),
            sg('x', 'b/r'),
            rt(' is paid by Johann.'),
        ]
        assertEquals(body, parser.parse(text, DefaultRulesText))
        assertEquals(text, parser.unparse(body, RulesTextType).content.join(''))
    }

    protected String serialize(textType) {
        textType.content.collect {
            if (it instanceof String) {
                it
            } else if (it instanceof JAXBElement) {
                if (it.name.localPart == 'br') {
                    "<br />"
                } else if (it.name.localPart == 'reminder') {
                    "<reminder>${serialize(it.value)}</reminder>"
                } else {
                    throw new IllegalArgumentException("Unknown JAXBElement: ${it.name}")
                }
            } else {
                throw new IllegalArgumentException("Unknown Serializable: ${it.getClass().name}")
            }
        }.join('')
    }

    @Test
    void a_breaks() {
        def text = '\n\none\ntwo\n\nthree\n\n\n\nfour\n five\n'
        def body = [
            rt("one two three four five"),
        ]
        assertEquals(body, parser.parse(text, DefaultRulesText))
        assertEquals("one two three four five", serialize(parser.unparse(body, RulesTextType)))
    }

    @Test
    void complexRules() {
        def el = new RulesTextType()
        el.content.addAll([
            "{R}: ability ",
            new JAXBElement(new QName("reminder"), NonNormativeTextType, new NonNormativeTextType(content: [
                "(multi-line",
                new JAXBElement(new QName("br"), String, null),
                "\nreminder {R}{U})"
            ])),
        ])
        def body = [
            sg('R'),
            rt(": ability "),
            nnt("(multi-line"),
            lb(),
            nnt("reminder "),
            sg('R', 'U'),
            nnt(')')
        ]
        assertEquals(body, parser.parse(el))
        assertEquals('{R}: ability <reminder>(multi-line<br />\nreminder {R}{U})</reminder>', serialize(parser.unparse(body, el.getClass())))
    }

    @Test
    void complexFlavor() {
        def el = new NonNormativeTextType()
        el.content.addAll([
            "flavor",
            new JAXBElement<String>(new QName("br"), String, null),
            "\nauthor {R}{U} text",
        ])
        def body = [
            nnt("flavor"),
            lb(),
            nnt("author "),
            sg('R', 'U'),
            nnt(' text')
        ]
        assertEquals(body, parser.parse(el))
        assertEquals('flavor<br />\nauthor {R}{U} text', serialize(parser.unparse(body, el.getClass())))
    }

    @Test
    void guulDrazAssassin() {
        /*
        <rules-text>Level up {1}{B}
            <reminder>({1}{B}: Put a level counter on this. Level up only as a sorcery.)</reminder>
        </rules-text>
        */
        def rel = new NonNormativeTextType()
        rel.content << "({1}{B}: Put a level counter on this. Level up only as a sorcery.)"
        def el = new RulesTextType()
        el.content.addAll([
            "Level up {1}{B} ",
            new JAXBElement<NonNormativeTextType>(new QName("reminder"), NonNormativeTextType, rel)
        ])
        def body = [
            rt('Level up '),
            sg('1', 'B'),
            rt(' '),
            nnt('('),
            sg('1', 'B'),
            nnt(': Put a level counter on this. Level up only as a sorcery.)')
        ]
        assertEquals(body, parser.parse(el))
        assertEquals("Level up {1}{B} <reminder>({1}{B}: Put a level counter on this. Level up only as a sorcery.)</reminder>", serialize(parser.unparse(body, el.getClass())))
    }

}
