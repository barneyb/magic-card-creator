package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.CreatureLevel
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.core.DefaultCard
import com.barneyb.magic.creator.core.DefaultCreatureCard
import com.barneyb.magic.creator.core.DefaultFusedCard
import com.barneyb.magic.creator.core.DefaultPlaneswalkerCard
import com.barneyb.magic.creator.descriptor.CardSetImporter
import com.barneyb.magic.creator.descriptor.XmlCardSetReader
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
        new XmlCardSetReader(DynamicThemeTest.classLoader.getResource("test-set.xml")).read()
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
        new File((c.title ?: "Card #${System.identityHashCode(c)}").toLowerCase().replaceAll(/[^a-z0-9]+/, '-') + ".svg")
    }

    protected static emit(String title) {
        emit(card(title))
    }

    protected static emit(Card c) {
        if (emitted.put(c, true) == null) {
            file(c).text = XmlUtils.write(theme.layout(c))
            true
        } else {
            false
        }
    }

    protected static files() {
        cardset().cards.collect this.&file
    }

    static Map<Card, Boolean> emitted

    @BeforeClass
    static void setup() {
        emitted = new IdentityHashMap<>()
        theme = (DynamicTheme) new DynamicThemeLoader().load(DynamicThemeTest.classLoader.getResource("theme/default/descriptor.json"))
        (files() + PROOF_SHEET_FILE).findAll {
            it.exists()
        }*.delete()
    }

    @AfterClass
    static void cleanup() {
        def proofs = files().findAll {
            it.exists()
        }
        if (proofs.size() == 0) {
            return
        }
        PROOF_SHEET_FILE.text = """\
<html>
<head>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
</head>
<body>
${proofs.collect {
    it.deleteOnExit()
    it.text
}.join("\n")}
<p>Generated at: ${new Date()}
</body>
</html>"""
    }

    @Test
    void allNull() {
        emit(new Card() {
            LayoutType getLayoutType() { LayoutType.SPELL }
            String getTitle() { null }
            SymbolGroup getCastingCost() { null }
            Artwork getArtwork() { null }
            Artwork getOverArtwork() { null }
            List<String> getTypeParts() { null }
            boolean isType(String type) { false }
            List<String> getSubtypeParts() { null }
            boolean isSubtype(String subtype) { false }
            boolean isSemiEnchantment() { false }
            List<List<BodyItem>> getRulesText() { null }
            List<List<BodyItem>> getFlavorText() { null }
            List<ManaColor> getColors() { null }
            boolean isHybrid() { false }
            boolean isColorExplicit() { false }
            boolean isMultiColor() { false }
            List<ManaColor> getAlliedColors() { null }
            String getWatermarkName() { null }
            String getCopyright() { null }
            Rarity getRarity() { null }
            boolean isFused() { false }
            List<Card> getFusedCards() { null }
            CardSet getSet() { null }
            Integer getCardNumber() { null }
            Integer getSetCardCount() { null }
        })
        emit(new CreatureCard() {
            LayoutType getLayoutType() { LayoutType.CREATURE }
            String getPower() { null }
            String getToughness() { null }
            boolean isLeveler() { false }
            List<CreatureLevel> getLevels() { null }
            String getTitle() { null }
            SymbolGroup getCastingCost() { null }
            Artwork getArtwork() { null }
            Artwork getOverArtwork() { null }
            List<String> getTypeParts() { null }
            boolean isType(String type) { false }
            List<String> getSubtypeParts() { null }
            boolean isSubtype(String subtype) { false }
            boolean isSemiEnchantment() { false }
            List<List<BodyItem>> getRulesText() { null }
            List<List<BodyItem>> getFlavorText() { null }
            List<ManaColor> getColors() { null }
            boolean isHybrid() { false }
            boolean isColorExplicit() { false }
            boolean isMultiColor() { false }
            List<ManaColor> getAlliedColors() { null }
            String getWatermarkName() { null }
            String getCopyright() { null }
            Rarity getRarity() { null }
            boolean isFused() { false }
            List<Card> getFusedCards() { null }
            CardSet getSet() { null }
            Integer getCardNumber() { null }
            Integer getSetCardCount() { null }
        })
        emit(new DefaultCard())
        emit(new DefaultCreatureCard())
        if (theme.supports(LayoutType.PLANESWALKER)) {
            emit(new DefaultPlaneswalkerCard())
        }
        if (theme.supports(LayoutType.FUSE)) {
            emit(new DefaultFusedCard())
        }
    }

    @Test
    void white() {
        cherub()
    }

    @Test
    void blue() {
        counterspell()
    }

    @Test
    void black() {
        nightmare()
    }

    @Test
    void red() {
        sally()
    }

    @Test
    void green() {
        woods()
    }

    @Test
    void gold() {
        barney()
    }

    @Test
    void artifact() {
        axe()
    }

    @Test
    void land() {
        brothel()
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
    void woods() {
        emit('The Green Woods')
    }

    @Test
    void axe() {
        emit("Purphoros' Axe")
    }

    @Test
    void everythingElse() {
        cardset().cards.each {
            if (emit(it)) {
                println "emitted '$it.title' also"
            }
        }
    }

}
