package com.barneyb.magic.creator.descriptor.markdown

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.core.DefaultCard
import com.barneyb.magic.creator.core.DefaultCardSet
import com.barneyb.magic.creator.core.DefaultCreatureCard
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.core.SimpleArtwork
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import org.junit.Test

import static com.barneyb.magic.creator.descriptor.markdown.Assert.*
import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class MarkdownCardSetReaderTest {

    public static final URL TEST_SET_DESCRIPTOR = MarkdownCardSetReaderTest.classLoader.getResource("test_set.md")

    SymbolFactory sf = new DefaultSymbolFactory()

    final Card sally = new DefaultCreatureCard(
        title: "Sally",
        castingCost: sf.getCost("1R"),
        colors: [ManaColor.RED],
        artwork: new SimpleArtwork(
            new URL(TEST_SET_DESCRIPTOR, "artwork/sally.jpg"),
            "Sally Mann"
        ),
        typeParts: ["Creature"],
        subtypeParts: ["Human"],
        rulesText: [[
                        sf.getCost("UT"),
                        new DefaultRulesText(": Tap target creature and pay 1 life.")
                    ]],
        flavorText: [
            [new DefaultNonNormativeText("Sally doesn't like you.")],
            [new DefaultNonNormativeText("Or you.")],
        ],
        power: "1",
        toughness: "1"
    )

    protected def desc(String src) {
        new MarkdownCardSetReader(TEST_SET_DESCRIPTOR, new StringReader(src))
    }

    @Test
    void funkyChars() {
        def cs = desc("""
Test \\u00b6 Set
===
\\u00A9 Barney
""").read()
        assertEquals("Test \u00b6 Set", cs.title)
        assertEquals("TS", cs.key)
        assertEquals("\u00A9 Barney", cs.copyright)
    }

    @Test
    void setAttributes() {
        def cs = desc("""
Test Set
===
one
two

three

## Sally 1R

Creature - Human

*Sally eats souls.*

1/1
""").read()
        assertEquals("Test Set", cs.title)
        assertEquals("TS", cs.key)
        assertEquals("one two three", cs.copyright)
    }

    @Test
    void card() {
        def cs = desc("""
## Sally 1R

![Sally Mann](artwork/sally.jpg)

Creature - Human

{U}{T}: Tap target creature and pay 1 life.

*Sally doesn't like you.*

*Or you.*

1/1
""").read()
        assertEquals(1, cs.cardsInSet)
        assertCard(sally, cs.cards.first())
    }

    @Test
    void testSet() {
        assertCardSet(
            new DefaultCardSet("Test Set", "TS", "\u00A9 2014 Barney Boisvert", [
                sally,
                new DefaultCard(
                    title: "Counterspell",
                    castingCost: sf.getCost("uu"),
                    artwork: new SimpleArtwork(
                        new URL(TEST_SET_DESCRIPTOR, "artwork/counterspell.jpg"),
                        "Zueuk"
                    ),
                    typeParts: ["Instant"],
                    rulesText: [[
                        new DefaultRulesText("Counter target spell.")
                    ]],
                    flavorText: [[
                        new DefaultNonNormativeText("How about \"never\"."),
                        new DefaultLineBreak(),
                        new DefaultNonNormativeText("-- Barney of the Green Woods")
                    ]]
                ),
                new DefaultCreatureCard(
                    title: "Nightmare",
                    castingCost: sf.getCost("5B"),
                    artwork: new SimpleArtwork(
                        new URL(TEST_SET_DESCRIPTOR, "artwork/nightmare.jpg"),
                        "catfish08"
                    ),
                    typeParts: ["Creature"],
                    subtypeParts: ["Nightmare", "Horse"],
                    rulesText: [
                        [new DefaultRulesText("Flying")],
                        [new DefaultRulesText("Nightmare's power and toughness are each equal to the number of Swamps you control.")],
                    ],
                    flavorText: [[
                        new DefaultNonNormativeText("The thunder of its hooves beats dreams into despair."),
                    ]],
                    power: "*",
                    toughness: "*"
                ),
                new DefaultCreatureCard(
                    title: "Blitz Hellion",
                    castingCost: sf.getCost("3RG"),
                    artwork: new SimpleArtwork(
                        new URL(TEST_SET_DESCRIPTOR, "artwork/hellion.jpg"),
                        "Anthony S. Waters"
                    ),
                    typeParts: ["Creature"],
                    subtypeParts: ["Hellion"],
                    rulesText: [
                        [new DefaultRulesText("Trample, haste")],
                        [new DefaultRulesText("At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library.")],
                    ],
                    flavorText: [[
                        new DefaultNonNormativeText("Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate."),
                    ]],
                    power: "7",
                    toughness: "7"
                ),
                new DefaultCreatureCard(
                    title: "Barney of the Green Woods",
                    castingCost: sf.getCost("2WUBRG"),
                    artwork: new SimpleArtwork(
                        new URL(TEST_SET_DESCRIPTOR, "artwork/barney_fake.png"),
                        "Dolores Boisvert"
                    ),
                    typeParts: ["Legendary", "Enchantment", "Creature"],
                    subtypeParts: ["Human", "Legend"],
                    rulesText: [
                        [new DefaultRulesText("Indestructable, hexproof")],
                        [
                            sf.getCost("{1}{U}{G}{T}"),
                            new DefaultRulesText(": Every opponent dies in a fire unless all pay "),
                            sf.getCost("{X}{U}{B}{G}"),
                            new DefaultRulesText(" or "),
                            sf.getCost("{X}{R}{B}{W}"),
                            new DefaultRulesText(" or "),
                            sf.getCost("{X}{X}{B}"),
                            new DefaultRulesText(" where X equals your life total.")
                        ],
                    ],
                    flavorText: [[
                        new DefaultNonNormativeText("For the first time in his life, Barney felt warm. And hungry."),
                    ]],
                    power: "100",
                    toughness: "100"
                ),
                new DefaultCard(
                    title: "Elysian Brothel",
                    colors: [ManaColor.COLORLESS],
                    artwork: new SimpleArtwork(
                        new URL(TEST_SET_DESCRIPTOR, "artwork/brothel.jpg"),
                        "God of War: Ascension"
                    ),
                    typeParts: ["Legendary", "Land"],
                    rulesText: [[
                        new DefaultRulesText("All legendary creatures, gods, and angels, get -1/-1.")
                    ]],
                    flavorText: [[
                        new DefaultNonNormativeText("They are selective in their clientele, but their service legendary."),
                    ]],
                ),
                new DefaultCreatureCard(
                    title: "Sleeping Cherub",
                    castingCost: sf.getCost("2WW"),
                    artwork: new SimpleArtwork(
                        new URL(TEST_SET_DESCRIPTOR, "artwork/sally.jpg"),
                        "Sally Mann"
                    ),
                    typeParts: ["Creature"],
                    subtypeParts: ["Angel"],
                    rulesText: [[
                        new DefaultRulesText("Flying, double strike, lifelink")
                    ]],
                    flavorText: [[
                        new DefaultNonNormativeText("Serra's children are angels. And fierce."),
                    ]],
                    power: "2",
                    toughness: "4"
                )
            ]),
            new MarkdownCardSetReader(TEST_SET_DESCRIPTOR).read()
        )
    }

    @Test
    void constructor_equivalence() {
        def cl = getClass().classLoader
        def path = "test_set.md"
        def res = cl.getResource(path)
        def url = new MarkdownCardSetReader(res).read()
        def stream = new MarkdownCardSetReader(res, res.newInputStream())
        def reader = new MarkdownCardSetReader(res, res.newReader())
        assertCardSet(url, stream.read())
        assertCardSet(url, reader.read())
    }

}
