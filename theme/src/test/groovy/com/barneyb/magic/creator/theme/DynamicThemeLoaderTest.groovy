package com.barneyb.magic.creator.theme

import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DynamicThemeLoaderTest {

    @Test
    void descriptor() {
        def t = new DynamicThemeLoader().load(getClass().classLoader.getResource("theme/default/descriptor.json"))
        println t
    }

}
