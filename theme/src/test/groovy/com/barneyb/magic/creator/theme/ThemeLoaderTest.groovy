package com.barneyb.magic.creator.theme

import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class ThemeLoaderTest {

    @Test
    void descriptor() {
        def t = new ThemeLoader().load(getClass().classLoader.getResource("theme/default/descriptor.json"))
        println t
    }

}
