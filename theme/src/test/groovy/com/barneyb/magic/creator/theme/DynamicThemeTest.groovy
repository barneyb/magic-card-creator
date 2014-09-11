package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.descriptor.CardSetImporter
import com.barneyb.magic.creator.util.XmlUtils
import groovy.transform.Memoized
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DynamicThemeTest {

    public static final File PROOF_SHEET_FILE = new File("proof-default-theme.html")
    static DynamicTheme theme

    @Memoized
    protected static CardSet cardset() {
        new CardSetImporter().fromUrl(DynamicThemeTest.classLoader.getResource("test-set.xml"))
    }

    protected static Card card(String title) {
        def c = cardset().cards.find {
            it.title == title
        }
        if (c == null) {
            throw new IllegalArgumentException("No card named '$title' was found.")
        }
        c
    }

    protected static file(Card c) {
        new File(c.title.toLowerCase().replaceAll(/[^a-z0-9]+/, '-') + ".svg")
    }

    protected static emit(String title) {
        emit(card(title))
    }

    protected static emit(Card c) {
        file(c).text = XmlUtils.write(theme.layout(c))
    }

    protected static files() {
        cardset().cards.collect this.&file
    }

    @BeforeClass
    static void deleteExisting() {
        theme = (DynamicTheme) new ThemeLoader().load(DynamicThemeTest.classLoader.getResource("theme/default/descriptor.json"))
        (files() + PROOF_SHEET_FILE).findAll {
            it.exists()
        }*.delete()
    }

    @AfterClass
    static void proofsheet() {
        PROOF_SHEET_FILE.text = """\
<html>
<head>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
</head>
<body>
${files().findAll {
    it.exists()
}.collect {
    it.deleteOnExit()
    it.text
}.join("\n")}
<p>Generated at: ${new Date()}
</body>
</html>"""
    }

    @Test
    void sally() {
        emit('Sally')
    }

    @Test
    void cherub() {
        emit('Sleeping Cherub')
    }

    @Test
    void counterspell() {
        emit('Counterspell')
    }

    @Test
    void nightmare() {
        emit('Nightmare')
    }

    @Test
    void hellion() {
        emit('Blitz Hellion')
    }

    @Test
    void barney() {
        emit('Barney of the Green Woods')
    }

    @Test
    void brothel() {
        emit('Elysian Brothel')
    }

    @Test
    void everythingElse() {
        cardset().cards.each {
            if (! file(it).exists()) {
                println "emitting $it.title also"
                emit(it)
            }
        }
    }

}
