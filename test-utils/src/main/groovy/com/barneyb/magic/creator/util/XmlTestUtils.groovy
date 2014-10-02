package com.barneyb.magic.creator.util

import nu.xom.Builder
import nu.xom.Serializer
import nu.xom.canonical.Canonicalizer

import static junit.framework.Assert.*

/**
 *
 *
 * @author barneyb
 */
class XmlTestUtils {

    static void assertXmlResource(String pathOfExpected, String actual) {
        assertXml(XmlTestUtils.classLoader.getResourceAsStream(pathOfExpected).text, actual)
    }

    static void assertXml(String expected, String actual) {
        expected = prettify(expected)
        actual = prettify(actual)
        assertEquals(expected, actual)
    }

    static String prettify(String xml) {
        // canonicalize
        def out = new ByteArrayOutputStream()
        def c = new Canonicalizer(out, false) // strip comments, thus allowing comment annotations in the test files
        c.write(new Builder().build(xml, null))
        xml = out.toString()
        // prettify
        out = new ByteArrayOutputStream()
        def s = new Serializer(out)
        s.indent = 4
        s.lineSeparator = "\n"
        s.write(new Builder().build(xml, null))
        out.toString()
    }

}
