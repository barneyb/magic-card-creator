package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.SymbolGroup
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
            @Override
            LayoutType getLayoutType() {
                LayoutType.CREATURE
            }

            @Override
            String getTitle() {
                return null
            }

            @Override
            SymbolGroup getCastingCost() {
                return null
            }

            @Override
            Artwork getArtwork() {
                return null
            }

            @Override
            Artwork getOverArtwork() {
                return null
            }

            @Override
            List<String> getTypeParts() {
                return null
            }

            @Override
            boolean isType(String type) {
                return false
            }

            @Override
            List<String> getSubtypeParts() {
                return null
            }

            @Override
            boolean isSubtype(String subtype) {
                return false
            }

            @Override
            boolean isSemiEnchantment() {
                return false
            }

            @Override
            List<List<BodyItem>> getRulesText() {
                return null
            }

            @Override
            List<List<BodyItem>> getFlavorText() {
                return null
            }

            @Override
            List<ManaColor> getColors() {
                return null
            }

            @Override
            boolean isHybrid() {
                return false
            }

            @Override
            boolean isColorExplicit() {
                return false
            }

            @Override
            boolean isMultiColor() {
                return false
            }

            @Override
            List<ManaColor> getAlliedColors() {
                return null
            }

            @Override
            String getWatermarkName() {
                return null
            }

            @Override
            String getCopyright() {
                return null
            }

            @Override
            Rarity getRarity() {
                return null
            }

            @Override
            boolean isFused() {
                return false
            }

            @Override
            List<Card> getFusedCards() {
                return null
            }

            @Override
            CardSet getSet() {
                return null
            }

            @Override
            Integer getCardNumber() {
                return null
            }

            @Override
            Integer getSetCardCount() {
                return null
            }
        })
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
