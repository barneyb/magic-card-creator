package com.barneyb.magic.creator

import com.barneyb.magic.creator.asset.RemoteImage
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.AbilityText
import com.barneyb.magic.creator.compositor.CompoundImageAsset
import com.barneyb.magic.creator.compositor.FlavorText
import com.barneyb.magic.creator.compositor.Paragraph
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.descriptor.Card
import com.barneyb.magic.creator.descriptor.CardSet
import com.barneyb.magic.creator.descriptor.CostType
import com.barneyb.magic.creator.descriptor.FrameBaseType
import com.barneyb.magic.creator.descriptor.FrameModifier

/**
 *
 * @author bboisvert
 */
class Cards {

    static def _cl = Cards.classLoader

    static CardSet set() {
        def s = new CardSet("Test Set", "\u00A9 2014 Barney Boisvert")
        s << new Card(
            title: "Sally",
            costString: "1R",
            artwork: _cl.getResource("artwork/sally.jpg"),
            type: "Creature",
            subtype: "Human",
            body: """
{U}{T}: Tap target creature and pay 1 life.

{Sally doesn't like you.}

{Or you.}
""",
            power: "1",
            toughness: "1",
            artist: "Sally Mann"
        )
        s << new Card(
            title: "Counterspell",
            costString: "UU",
            artwork: _cl.getResource("artwork/counterspell.jpg"),
            type: "Instant",
            body: """
Counter target spell.

{How about "never".}
{    -- Barney of the Green Woods}
""",
            artist: "Zueuk"
        )
        s << new Card(
            title: "Nightmare",
            costString: "5B",
            artwork: _cl.getResource("artwork/nightmare.jpg"),
            type: "Creature",
            subtype: "Nightmare Horse",
            body: """
Flying

Nightmare's power and toughness are each equal to the number of Swamps you control.

{The thunder of its hooves beats dreams into despair.}
""",
            power: "*",
            toughness: "*",
            artist: "catfish08"
        )
        s << new Card(
            title: "Blitz Hellion",
            costString: "2RG",
            artwork: _cl.getResource("artwork/hellion.jpg"),
            type: "Creature",
            subtype: "Hellion",
            body: """
Trample, haste

At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library.

{Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate.}
""",
            power: "7",
            toughness: "7",
            artist: "Anthony S. Waters"
        )
        s << new Card(
            title: "Barney of the Green Woods",
            costString: "2WUBRG",
            artwork: _cl.getResource("artwork/barney_fake.png"),
            type: "Legendary Enchantment Creature",
            subtype: "Human Legend",
            body: """
Indestructable, hexproof

{1}{U}{G}{T}: Every opponent dies in a fire unless all pay {X}{U}{B}{G} or {X}{R}{B}{W} or {X}{X}{B} where X equals your life total.

{For the first time in his life, Barney felt warm. And hungry.}
""",
            power: "100",
            toughness: "100",
            artist: "Dolores Boisvert"
        )
        s << new Card(
            title: "Elysian Brothel",
            artwork: _cl.getResource("artwork/brothel.jpg"),
            type: "Legendary Land",
            body: """
All legendary creatures, gods, and angels, get -1/-1.

{They are selective in their clientele, but their service legendary.}
""",
            artist: "God of War: Ascension"
        )
        s
    }

    static Card counterspell() {
        set().get("Counterspell")
    }

    static RenderModel counterspell(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.BLUE),
            title: "Counterspell",
            cost: [CostType.BLUE, CostType.BLUE].collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(_cl.getResource("artwork/counterspell.jpg")),
            type: "Instant",
            body: [
                [
                    new AbilityText("Counter target spell."),
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("How about \"never\"."),
                ],
                [
                    new FlavorText("    -- Barney of the Green Woods"),
                ]
            ],
            artist: "Zueuk",
            footer: "\u00A9 2014 Barney Boisvert (2/6)"
        )
    }

    static Card nightmare() {
        set().get("Nightmare")
    }

    static RenderModel nightmare(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.BLACK + FrameModifier.Type.CREATURE),
            title: "Nightmare",
            cost: [CostType.COLORLESS_5, CostType.BLACK].collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(_cl.getResource("artwork/nightmare.jpg")),
            type: "Creature \u2013 Nightmare Horse",
            body: [
                [
                    new AbilityText("Flying"),
                ],
                [
                    new Paragraph()
                ],
                [
                    new AbilityText("Nightmare's power and toughness are each equal to the number of Swamps you control."),
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("The thunder of its hooves beats dreams into despair."),
                ]
            ],
            powerToughness: "* / *",
            whiteFooterText: true,
            artist: "catfish08",
            footer: "\u00A9 2014 Barney Boisvert (3/6)"
        )
    }

    static Card sally() {
        set().get("Sally")
    }

    static RenderModel sally(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.RED + FrameModifier.Type.CREATURE),
            title: "Sally",
            cost: [CostType.COLORLESS_1, CostType.RED].collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(_cl.getResource("artwork/sally.jpg")),
            type: "Creature \u2013 Human",
            body: [
                [
                    new CompoundImageAsset([
                        rs.small.getImageAsset(CostType.BLUE),
                        rs.small.getImageAsset(CostType.TAP)
                    ]),
                    new AbilityText(": Tap target creature and pay 1 life."),
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("Sally doesn't like you.")
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("Or you.")
                ]
            ],
            powerToughness: "1 / 1",
            artist: "Sally Mann",
            footer: "\u00A9 2014 Barney Boisvert (1/6)"
        )
    }

    static Card barney() {
        set().get("Barney of the Green Woods")
    }

    static RenderModel barney(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.GOLD + FrameModifier.Type.ENCHANTMENT_CREATURE),
            title: "Barney of the Green Woods",
            cost: [CostType.COLORLESS_2, CostType.WHITE, CostType.BLUE, CostType.BLACK, CostType.RED, CostType.GREEN].collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(_cl.getResource("artwork/barney_fake.png")),
            type: "Legendary Enchantment Creature \u2013 Human Legend",
            body: [
                [
                    new AbilityText("Indestructable, hexproof"),
                ],
                [
                    new Paragraph()
                ],
                [
                    new CompoundImageAsset([
                        rs.small.getImageAsset(CostType.COLORLESS_1),
                        rs.small.getImageAsset(CostType.BLUE),
                        rs.small.getImageAsset(CostType.GREEN),
                        rs.small.getImageAsset(CostType.TAP)
                    ]),
                    new AbilityText(": Every opponent dies in a fire unless all pay "),
                    new CompoundImageAsset([
                        rs.small.getImageAsset(CostType.COLORLESS_X),
                        rs.small.getImageAsset(CostType.BLUE),
                        rs.small.getImageAsset(CostType.BLACK),
                        rs.small.getImageAsset(CostType.GREEN)
                    ]),
                    new AbilityText(" or "),
                    new CompoundImageAsset([
                        rs.small.getImageAsset(CostType.COLORLESS_X),
                        rs.small.getImageAsset(CostType.RED),
                        rs.small.getImageAsset(CostType.BLACK),
                        rs.small.getImageAsset(CostType.WHITE)
                    ]),
                    new AbilityText(" or "),
                    new CompoundImageAsset([
                        rs.small.getImageAsset(CostType.COLORLESS_X),
                        rs.small.getImageAsset(CostType.COLORLESS_X),
                        rs.small.getImageAsset(CostType.BLACK)
                    ]),
                    new AbilityText(" where X equals your life total."),
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("For the first time in his life, Barney felt warm. And hungry.")
                ]
            ],
            powerToughness: "100/100",
            artist: "Dolores Boisvert",
            footer: "\u00A9 2014 Barney Boisvert (5/6)"
        )
    }

    static Card hellion() {
        set().get("Blitz Hellion")
    }

    static RenderModel hellion(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.GOLD + FrameModifier.Type.CREATURE + FrameModifier.Dual.RED_GREEN),
            title: "Blitz Hellion",
            cost: [CostType.COLORLESS_2, CostType.RED, CostType.GREEN].collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(_cl.getResource("artwork/hellion.jpg")),
            type: "Creature \u2013 Hellion",
            body: [
                [
                    new AbilityText("Trample, haste"),
                ],
                [
                    new Paragraph()
                ],
                [
                    new AbilityText("At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library."),
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate.")
                ]
            ],
            powerToughness: "7 / 7",
            artist: "Anthony S. Waters",
            footer: "\u00A9 2014 Barney Boisvert (4/6)"
        )
    }

    static Card brothel() {
        set().get("Elysian Brothel")
    }

    static RenderModel brothel(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.LAND),
            title: "Elysian Brothel",
            artwork: new RemoteImage(_cl.getResource("artwork/brothel.jpg")),
            type: "Legendary Land",
            body: [
                [
                    new AbilityText("All legendary creatures, gods, and angels, get -1/-1."),
                ],
                [
                    new Paragraph()
                ],
                [
                    new FlavorText("They are selective in their clientele, but their service legendary.")
                ]
            ],
            whiteFooterText: true,
            artist: "God of War: Ascension",
            footer: "\u00A9 2014 Barney Boisvert (6/6)"
        )
    }
}
