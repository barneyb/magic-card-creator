package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.descriptor.CardSetImporter
import com.barneyb.magic.creator.util.XmlUtils
import groovy.transform.Memoized
import org.junit.After
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DefaultThemeTest {

    @Memoized
    protected CardSet cardset() {
        new CardSetImporter().fromUrl(getClass().classLoader.getResource("test-set.xml"))
    }

    protected Card card(String title) {
        def c = cardset().cards.find {
            it.title == title
        }
        if (c == null) {
            throw new IllegalArgumentException("No card named '$title' was found.")
        }
        c
    }

    @After
    void proofsheet() {
        new File("proof-default-theme.html").text = """\
<html>
<head>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
</head>
<body>
<embed src="sally.svg" />
<embed src="cherub.svg" />
<embed src="counterspell.svg" />
<embed src="nightmare.svg" />
<embed src="hellion.svg" />
<embed src="barney.svg" />
<embed src="brothel.svg" />
</body>
</html>"""
    }

    @Test
    void sally() {
        new File("sally.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Sally')))
    }

    @Test
    void cherub() {
        new File("cherub.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Sleeping Cherub')))
    }

    @Test
    void counterspell() {
        new File("counterspell.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Counterspell')))
    }

    @Test
    void nightmare() {
        new File("nightmare.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Nightmare')))
    }

    @Test
    void hellion() {
        new File("hellion.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Blitz Hellion')))
    }

    @Test
    void barney() {
        new File("barney.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Barney of the Green Woods')))
    }

    @Test
    void brothel() {
        new File("brothel.svg").text = XmlUtils.write(new DefaultTheme().layout(card('Elysian Brothel')))
    }

}
