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
    
}
