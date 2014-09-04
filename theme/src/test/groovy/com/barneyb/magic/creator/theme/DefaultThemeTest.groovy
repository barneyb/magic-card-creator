package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.descriptor.CardSetImporter
import com.barneyb.magic.creator.util.XmlUtils
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DefaultThemeTest {

    @Test
    void getBarneyLaid() {
        def cs = new CardSetImporter().fromUrl(getClass().classLoader.getResource("test-set.xml"))
        def beb = cs.cards.find {
            it.title == 'Barney of the Green Woods'
        }
        def svg = XmlUtils.write(new DefaultTheme().layout(beb))
        new File("barney.svg").text = svg
    }

}
