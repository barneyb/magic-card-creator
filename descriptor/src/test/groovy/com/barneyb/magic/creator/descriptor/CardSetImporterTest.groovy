package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.PlaneswalkerCard
import com.barneyb.magic.creator.api.Rarity
import org.junit.Test

import static junit.framework.Assert.*
/**
 *
 *
 * @author barneyb
 */
@Mixin(TestMixin)
class CardSetImporterTest {

    protected assertRelativeUrl(URL base, String expected, URL actual) {
        assertEquals(new URL(base, expected), actual)
    }

    @Test
    void allInOne() {
        def descriptor = getClass().classLoader.getResource("all-in-one.xml")
        def relativeUrl = this.&assertRelativeUrl.curry(descriptor)
        def cs = new CardSetImporter().fromUrl(descriptor)
        def title = "All In One"
        def key = "AiO"
        def cardCount = 9
        def copyright = "\u00A9 Wizards of the Coast"
        assertEquals(title, cs.title)
        assertEquals(key, cs.key)
        assertEquals(copyright, cs.copyright)
        // check the names...
        assertEquals([
            "Nykthos, Shrine to Nyx",
            "Lightning Bolt",
            "White Knight",
            "Garruk, Apex Predator",
            "Guul Draz Assassin",
            "Dimir Guildgate",
            "Transguild Courier",
            "Paralyze",
            "Goblin Bully"
        ], cs.cards*.title)
        def (shrine, bolt, knight, garruk, assassin, gate, courier, paralyze, bully) = cs.cards

        Card c = shrine
        assertEquals(Rarity.RARE, c.rarity)
        assertEquals(1, c.cardNumber)
        relativeUrl("artwork/nykthos-shrine-to-nyx.jpg", c.artwork.url)
        assertEquals("Jung Park", c.artwork.artist)
        assertEquals(null, c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('-'), c.colors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(null, c.subtypeParts)
        assertEquals(["Legendary", "Land"], c.typeParts)
        assertEquals(null, c.watermarkName)
        assertEquals([
            [
                sg('T'),
                rt(": Add "),
                sg('1'),
                rt(" to your mana pool.")
            ],
            [
                sg('2'),
                rt(", "),
                sg('T'),
                rt(': Choose a color. Add to your mana pool an amount of mana of that color equal to your devotion to that color. '),
                nnt('(Your devotion to a color is the number of mana symbols of that color in the mana costs of permanents you control.'),
                lb(),
                nnt('It does not include mana symbols in card bodies.)')
            ],
        ], c.rulesText)
        assertEquals(null, c.flavorText)

        c = bolt
        assertEquals(Rarity.COMMON, c.rarity)
        assertEquals(2, c.cardNumber)
        relativeUrl("artwork/lightning-bolt.jpg", c.artwork.url)
        assertEquals("Christopher Moeller", c.artwork.artist)
        assertEquals(sg('r'), c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('r'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(null, c.subtypeParts)
        assertEquals(['Instant'], c.typeParts)
        assertEquals(null, c.watermarkName)
        assertEquals([[rt('Lightning Bolt deals 3 damage to target creature or player.')]], c.rulesText)
        assertEquals([[nnt('The sparkmage shrieked, calling on the rage of the storms of his youth. To his surprise, the sky responded with a fierce energy he\'d never thought to see again.')]], c.flavorText)

        c = knight as CreatureCard
        assertEquals("2", c.power)
        assertEquals("2", c.toughness)
        assertEquals(false, c.leveler)
        assertEquals(null, c.levels)
        assertEquals(Rarity.UNCOMMON, c.rarity)
        assertEquals(3, c.cardNumber)
        relativeUrl("artwork/white-knight.jpg", c.artwork.url)
        assertEquals("Christopher Moeller", c.artwork.artist)
        assertEquals(sg('w', 'w'), c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('w'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Human', 'Knight'], c.subtypeParts)
        assertEquals(['Creature'], c.typeParts)
        assertEquals(null, c.watermarkName)
        assertEquals([
            [
                rt('First strike '),
                nnt('(This creature deals combat damage before creatures without first strike.)')
            ],
            [
                rt('Protection from black '),
                nnt('(This creature can\'t be blocked, targeted, dealt damage, or enchanted by anything black.)')
            ]
        ], c.rulesText)
        assertEquals(null, c.flavorText)

        c = garruk as PlaneswalkerCard
        assertEquals("5", c.loyalty)
        assertEquals(4, c.loyaltyAbilities.size())
        assertEquals('+1', c.loyaltyAbilities[0].cost)
        assertEquals([[rt('Destroy another target planeswalker.')]], c.loyaltyAbilities[0].rulesText)
        assertEquals('+1', c.loyaltyAbilities[1].cost)
        assertEquals([[rt('Put a 3/3 black Beast creature token with deathtouch onto the battlefield.')]], c.loyaltyAbilities[1].rulesText)
        assertEquals('-3', c.loyaltyAbilities[2].cost)
        assertEquals([[rt('Destroy target creature. You gain life equal to its toughness.')]], c.loyaltyAbilities[2].rulesText)
        assertEquals('-8', c.loyaltyAbilities[3].cost)
        assertEquals([[rt('Target opponent gets an emblem with "Whenever a creature attacks you, it gets +5/+5 and gains trample until end of turn."')]], c.loyaltyAbilities[3].rulesText)
        assertEquals(Rarity.MYTHIC_RARE, c.rarity)
        assertEquals(null, c.flavorText)
        assertEquals(4, c.cardNumber)
        relativeUrl("artwork/garruk-apex-predator.jpg", c.artwork.url)
        assertEquals("Tyler Jacobson", c.artwork.artist)
        assertEquals(sg('5', 'b', 'g'), c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('b', 'g'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(true, c.multiColor)
        relativeUrl("artwork/garruk-apex-predator-over.jpg", c.overArtwork.url)
        assertEquals("Tyler Jacobson", c.overArtwork.artist)
        assertEquals(null, c.rulesText)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Garruk'], c.subtypeParts)
        assertEquals(['Planeswalker'], c.typeParts)
        assertEquals(null, c.watermarkName)

        c = assassin as CreatureCard
        assertEquals("1", c.power)
        assertEquals("1", c.toughness)
        assertEquals(true, c.leveler)
        assertEquals(2, c.levels.size())
        assertEquals("2-3", c.levels[0].label)
        assertEquals("2", c.levels[0].power)
        assertEquals("2", c.levels[0].toughness)
        assertEquals([[sg('B'), rt(', '), sg('T'), rt(': Target creature gets -2/-2 until end of turn.')]], c.levels[0].rulesText)
        assertEquals("4+", c.levels[1].label)
        assertEquals("4", c.levels[1].power)
        assertEquals("4", c.levels[1].toughness)
        assertEquals([[sg('B'), rt(', '), sg('T'), rt(': Target creature gets -4/-4 until end of turn.')]], c.levels[1].rulesText)
        assertEquals(Rarity.RARE, c.rarity)
        assertEquals(null, c.flavorText)
        assertEquals(5, c.cardNumber)
        relativeUrl("artwork/guul-draz-assassin.jpg", c.artwork.url)
        assertEquals("James Ryman", c.artwork.artist)
        assertEquals(sg('b'), c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('b'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals([[rt('Level up '), sg('1', 'b'), rt(' '), nnt('('), sg('1', 'b'), nnt(': Put a level counter on this. Level up only as a sorcery.)')]], c.rulesText)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Vampire', 'Assassin'], c.subtypeParts)
        assertEquals(['Creature'], c.typeParts)
        assertEquals(null, c.watermarkName)

        c = gate
        assertEquals(Rarity.COMMON, c.rarity)
        assertEquals([[nnt('In the cold corridors of the undercity, misinformation unfurls from Duskmantle like a spreading stain.')]], c.flavorText)
        assertEquals(6, c.cardNumber)
        relativeUrl("artwork/dimir-guildgate.jpg", c.artwork.url)
        assertEquals("Cliff Childs", c.artwork.artist)
        assertEquals(null, c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('-'), c.colors)
        assertEquals(mcs('u', 'b'), c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals([
            [rt('Dimir Guildgate enters the battlefield tapped.')],
            [sg('t'), rt(': Add '), sg('u') , rt(' or '), sg('b'), rt(' to your mana pool.')],
        ], c.rulesText)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Gate'], c.subtypeParts)
        assertEquals(['Land'], c.typeParts)
        assertEquals('dimir', c.watermarkName)

        c = courier as CreatureCard
        assertEquals("3", c.power)
        assertEquals("3", c.toughness)
        assertEquals(false, c.leveler)
        assertEquals(null, c.levels)
        assertEquals(Rarity.UNCOMMON, c.rarity)
        assertEquals([[nnt('Reluctant to meet face to face, the leaders of the ten guilds prefer to do official business through a go-between immune to bribes and threats.')]], c.flavorText)
        assertEquals(7, c.cardNumber)
        relativeUrl("artwork/transguild-courier.jpg", c.artwork.url)
        assertEquals("John Avon", c.artwork.artist)
        assertEquals(sg('4'), c.castingCost)
        assertEquals(true, c.colorExplicit)
        assertEquals(mcs('w', 'u', 'b', 'r', 'g'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(true, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals([[rt('Transguild Courier is all colors '), nnt('(even if this card isn\'t in play)'), rt('.')]], c.rulesText)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Golem'], c.subtypeParts)
        assertEquals(['Artifact', 'Creature'], c.typeParts)
        assertEquals(null, c.watermarkName)

        c = paralyze
        assertEquals(Rarity.COMMON, c.rarity)
        assertEquals(null, c.flavorText)
        assertEquals(8, c.cardNumber)
        relativeUrl("artwork/paralyze.jpg", c.artwork.url)
        assertEquals("Anson Maddocks", c.artwork.artist)
        assertEquals(sg('b'), c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('b'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals([
            [rt('Enchant creature')],
            [rt('When Paralyze enters the battlefield, tap enchanted creature.')],
            [rt('Enchanted creature doesn\'t untap during its controller\'s untap step.')],
            [rt('At the beginning of the upkeep of enchanted creature\'s controller, that player may pay '), sg('4'), rt('. If he or she does, untap the creature.')],
        ], c.rulesText)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Aura'], c.subtypeParts)
        assertEquals(['Enchantment'], c.typeParts)
        assertEquals(null, c.watermarkName)

        c = bully as CreatureCard
        assertEquals("2", c.power)
        assertEquals("1", c.toughness)
        assertEquals(false, c.leveler)
        assertEquals(null, c.levels)
        assertEquals(Rarity.COMMON, c.rarity)
        assertEquals([[nnt('It\'s easy to stand head and shoulders over those with no backbone.'), lb(), nnt('-- King Grahznok')]], c.flavorText)
        assertEquals(9, c.cardNumber)
        relativeUrl("artwork/goblin-bully.jpg", c.artwork.url)
        assertEquals("Pete Venters", c.artwork.artist)
        assertEquals(sg('1', 'r'), c.castingCost)
        assertEquals(false, c.colorExplicit)
        assertEquals(mcs('r'), c.colors)
        assertEquals(null, c.alliedColors)
        assertEquals(copyright, c.copyright)
        assertEquals(false, c.multiColor)
        assertEquals(null, c.overArtwork)
        assertEquals(null, c.rulesText)
        assertEquals(false, c.semiEnchantment)
        assertEquals(cardCount, c.setCardCount)
        assertEquals(title, c.setTitle)
        assertEquals(key, c.setKey)
        assertEquals(['Goblin'], c.subtypeParts)
        assertEquals(['Creature'], c.typeParts)
        assertEquals(null, c.watermarkName)
    }

}
