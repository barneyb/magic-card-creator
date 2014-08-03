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
        if (c.land) {
            frame = FrameBaseType.LAND
        } else if (c.artifact) {
            frame = FrameBaseType.ARTIFACT
        } else if (c.colors.size() > 1) {
            frame = FrameBaseType.GOLD
        } else {
            frame = FrameBaseType.valueOf(c.colors.first().name()) // kludge!
        }
        if (c.colors.size() == 2) {
            frame += FrameModifier.Dual.valueOf(c.colors*.name().join("_"))
        }
        def m = new RenderModel(
            title: c.title,
            cost: c.cost.collect(rs.large.&getImageAsset),
            artwork: new RemoteImage(new URL(c.artwork)),
            type: c.type + (c.subtyped ? " \u2013 $c.subtype" : ''),
            whiteFooterText: frame.whiteFooterText,
            artist: c.artist,
        )
        if (c.creature) {
            frame += FrameModifier.Animated.CREATURE
            m.powerToughness = c.power + '/' + c.toughness
        }
        if (c.enchantment && (c.artifact || c.creature)) {
            frame += FrameModifier.Enchanted.ENCHANTMENT
        }
        m.frame = rs.frames.getImageAsset(frame)
        m.body = BodyParser.parse(c.body).collect { line ->
            def grouped = []
            def last = null
            line.collect {
                it instanceof CostType ? rs.small.getImageAsset(it) : (Renderable) it
            }.each {
                if (it instanceof ImageAsset) {
                    if (last instanceof CompoundImageAsset) {
                        last.add(it)
                        return
                    } else if (last instanceof ImageAsset) {
                        grouped.remove((int) grouped.size() - 1)
                        it = new CompoundImageAsset([last, it])
                    }
                }
                grouped << it
                last = it
            }
            grouped
        }

        if (c.hasSet()) {
            m.footer = "$c.footer ($c.cardOfSet/$c.cardsInSet)"
        }
        m
    }

    ImageAsset frame

    String title

    List<ImageAsset> cost = []

    ImageAsset artwork

    String type

    List<List<Renderable>> body

    Set<ImageAsset> getBodyIcons() {
        def icons = [] as Set
        def doIt
        doIt = {
            if (it instanceof ImageAsset) {
                icons << it
            } else if (it instanceof Collection) {
                it.each doIt
            }
        }
        body.each doIt
        icons
    }

    boolean isPowerToughnessVisible() {
        powerToughness != null
    }

    // might be null
    String powerToughness

    boolean whiteFooterText = false

    String artist

    String footer

}
