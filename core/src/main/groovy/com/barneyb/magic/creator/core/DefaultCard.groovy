package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.Symbol

/**
 *
 *
 * @author barneyb
 */
class DefaultCard implements Card {

    CardSet set

    String title

    List<Symbol> castingCost = null

    Artwork artwork = null

    Artwork overArtwork = null

    List<String> typeParts = null

    List<String> subtypeParts = null

    @Override
    LayoutType getLayoutType() {
        typeParts*.toLowerCase().contains("land") ? LayoutType.LAND : LayoutType.SPELL
    }

    @Override
    boolean isSemiEnchantment() {
        def lctps = typeParts*.toLowerCase()
        lctps.contains("enchantment") &&
            (lctps.contains("creature") ||
                lctps.contains("artifact")
            )
    }

    List<List<BodyItem>> rulesText = null

    List<List<BodyItem>> flavorText = null

    List<ManaColor> colors = null

    List<ManaColor> alliedColors = null

    boolean colorExplicit = false

    @Override
    boolean isHybrid() {
        multiColor && castingCost.any {
            it.hybrid
        }
    }

    @Override
    boolean isMultiColor() {
        colors.size() > 1
    }

    String watermarkName = null

    Rarity rarity = null

    List<Card> getFusedCards() {
        throw new UnsupportedOperationException("This is not a fused card.")
    }

    @Override
    String getCopyright() {
        set?.copyright
    }

    Integer cardNumber = null

    @Override
    Integer getSetCardCount() {
        set?.cards?.size()
    }

}
