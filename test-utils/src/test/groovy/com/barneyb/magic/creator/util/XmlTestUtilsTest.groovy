package com.barneyb.magic.creator.util

import org.junit.Test

import static junit.framework.Assert.*

/**
 *
 *
 * @author barneyb
 */
class XmlTestUtilsTest {

    @Test
    void prettify() {
        assertEquals("""\
<?xml version="1.0" encoding="UTF-8"?>
<root attr="one">
    <child>
        <grand name="fred"/>
    </child>
</root>
""", XmlTestUtils.prettify("<root attr='one'><child><grand name='fred' /><!-- sally? --></child></root>"))
    }

}
