package com.barneyb.magic.creator.util

import org.junit.Test

import static com.barneyb.magic.creator.util.XmlUtils.read
import static com.barneyb.magic.creator.util.XmlUtils.write
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 *
 *
 * @author barneyb
 */
class XmlUtilsTest {

    static final String XML = """\
<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22">
    <circle r="11" cx="11" cy="11" fill="#d0cac3" />
    <g transform="translate(-1.5 0) scale(0.5)">
        <g transform="matrix(1 0 0 1 12 39)">
            <text font-family="Goudy Old Style" font-weight="bold" font-size="53px">0</text>
        </g>
    </g>
</svg>
"""

    @Test
    void roundtrip() {
        def xml = write(read(XML))
        [
            "<svg",
            "#d0cac3",
            "matrix",
            "Goudy Old Style",
            "53px",
            ">0<",
        ].each {
            assertTrue("no '$it' found.", xml.contains(it))
        }
    }

}
