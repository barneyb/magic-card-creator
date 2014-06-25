package com.barneyb.magic.creator.compositor

import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RemoteImage
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.descriptor.BodyParser
import com.barneyb.magic.creator.descriptor.Card
import com.barneyb.magic.creator.descriptor.CostType
import com.barneyb.magic.creator.descriptor.FrameBaseType
import com.barneyb.magic.creator.descriptor.FrameModifier
import com.barneyb.magic.creator.descriptor.FrameType
import groovy.transform.Canonical

/**
 *
 * @author bboisvert
 */
@Canonical
class RenderModel {

    static RenderModel fromCard(Card c, RenderSet rs) {
        FrameType frame
        if (c.colors.size() > 1) {
            frame = FrameBaseType.GOLD
            if (c.colors.size() == 2) {
                frame += FrameModifier.Dual.valueOf(c.colors*.name().join("_"))
            }
        } else {
            frame = FrameBaseType.valueOf(c.colors.first().name()) // kludge!
        }
        def m = new RenderModel(
            title: c.title,
            cost: c.cost.collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(new URL(c.artwork)),
            type: c.type + (c.subtyped ? " \u2013 $c.subtype" : ''),
            whiteFooterText: frame == FrameBaseType.BLACK,
            artist: c.artist,
        )
        if (c.creature) {
            frame += c.enchantment ? FrameModifier.Type.ENCHANTMENT_CREATURE : FrameModifier.Type.CREATURE
            m.powerToughness = c.power + ((c.power ?: '?').length() > 1 && (c.toughness ?: '?').length() > 1 ? '/' : ' / ') + c.toughness
        }
        m.frame = rs.frames.getImageAsset(frame)
        m.body = BodyParser.parse(c.body).collect { line ->
                line.collect {
                    it instanceof CostType ? rs.small.getImageAsset(it) : (Renderable) it
                }
            }

        if (c.hasSet()) {
            m.footer = "$c.footer ($c.cardOfSet/$c.cardsInSet)"
        }
        m
    }

    ImageAsset frame

    String title

    List<ImageAsset> cost

    ImageAsset artwork

    String type

    List<List<Renderable>> body

    boolean isPowerToughnessVisible() {
        powerToughness != null
    }

    // might be null
    String powerToughness

    boolean whiteFooterText = false

    String artist

    String footer

}
