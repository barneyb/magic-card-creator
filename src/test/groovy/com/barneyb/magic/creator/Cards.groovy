package com.barneyb.magic.creator
import com.barneyb.magic.creator.asset.ClasspathImage
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.RenderableString
import com.barneyb.magic.creator.descriptor.CostType
import com.barneyb.magic.creator.descriptor.FrameBaseType
import com.barneyb.magic.creator.descriptor.FrameModifier
/**
 *
 * @author bboisvert
 */
class Cards {

    static RenderModel nightmare(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.BLACK + FrameModifier.Type.CREATURE),
            title: "Nightmare",
            cost: [CostType.COLORLESS_5, CostType.BLACK].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/nightmare.jpg"),
            type: "Creature \u2013 Nightmare Horse",
            body: [
                [
                    new RenderableString("Flying", false),
                ],
                [
                    new RenderableString("Nightmare's power and toughness are each equal to the number of Swamps you control.", false),
                ],
                [
                    new RenderableString("The thunder of its hooves beats dreams into despair.", true),
                ]
            ],
            powerToughness: "*/*",
            whiteFooterText: true,
            artist: "catfish08",
            footer: "\u00A9 2014 Barney Boisvert (3/5)"
        )
    }

    static RenderModel sally(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.RED + FrameModifier.Type.CREATURE),
            title: "Sally",
            cost: [CostType.COLORLESS_1, CostType.RED].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/sally.jpg"),
            type: "Creature \u2013 Human",
            body: [
                [
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.TAP),
                    new RenderableString(": Tap target creature and pay 1 life.", false),
                ],
                [
                    new RenderableString("Sally doesn't like you.", true)
                ],
                [
                    new RenderableString("Or you.", true)
                ]
            ],
            powerToughness: "1/1",
            artist: "Sally Mann",
            footer: "\u00A9 2014 Barney Boisvert (1/5)"
        )
    }

    static RenderModel barney(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.GOLD + FrameModifier.Type.ENCHANTMENT_CREATURE),
            title: "Barney of the Green Woods",
            cost: [CostType.COLORLESS_2, CostType.WHITE, CostType.BLUE, CostType.BLACK, CostType.RED, CostType.GREEN].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/barney.jpg"),
            type: "Legendary Enchantment Creature \u2013 Human Legend",
            body: [
                [
                    new RenderableString("Indestructable, hexproof", false),
                ],
                [
                    rs.small.getImageAsset(CostType.COLORLESS_1),
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.GREEN),
                    rs.small.getImageAsset(CostType.TAP),
                    new RenderableString(": Every opponent dies in a fire unless all pay ", false),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.BLUE),
                    rs.small.getImageAsset(CostType.BLACK),
                    rs.small.getImageAsset(CostType.GREEN),
                    new RenderableString(" or ", false),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.RED),
                    rs.small.getImageAsset(CostType.BLACK),
                    rs.small.getImageAsset(CostType.WHITE),
                    new RenderableString(" or ", false),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.COLORLESS_X),
                    rs.small.getImageAsset(CostType.BLACK),
                    new RenderableString(" where X equals your life total.", false),
                ],
                [
                    new RenderableString("For the first time in his life, Barney felt warm.  And hungry.", true)
                ]
            ],
            powerToughness: "100/100",
            artist: "Dolores Boisvert",
            footer: "\u00A9 2014 Fucking Barney! (5/5)"
        )
    }

    static RenderModel hellion(RenderSet rs) {
        new RenderModel(
            frame: rs.frames.getImageAsset(FrameBaseType.GOLD + FrameModifier.Type.CREATURE + FrameModifier.Dual.RED_GREEN),
            title: "Blitz Hellion",
            cost: [CostType.COLORLESS_2, CostType.RED, CostType.GREEN].collect(rs.large.&getImageAsset),
            artwork: new ClasspathImage("artwork/hellion.jpg"),
            type: "Creature \u2013 Hellion",
            body: [
                [
                    new RenderableString("Trample, haste", false),
                ],
                [
                    new RenderableString("At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library.", false),
                ],
                [
                    new RenderableString("Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate.", true)
                ]
            ],
            powerToughness: "7/7",
            artist: "Anthony S. Waters",
            footer: "\u00A9 2014 Fucking Barney! (4/5)"
        )
    }
}
