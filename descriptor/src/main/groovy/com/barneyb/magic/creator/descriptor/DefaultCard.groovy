package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.Symbol

/**
 *
 *
 * @author barneyb
 */
class DefaultCard implements Card {

    CardSet cardSet

    String title

    List<Symbol> castingCost = null

    Artwork artwork = null

    Artwork overArtwork = null

    List<String> typeParts = null

    List<String> subtypeParts = null

    @Override
    boolean isSemiEnchantment() {
        def lcps = typeParts*.toLowerCase()
        lcps.contains("enchantment") &&
            (lcps.contains("creature") ||
                lcps.contains("artifact")
            )
    }

    List<List<BodyItem>> rulesText = null

    List<List<BodyItem>> flavorText = null

    List<ManaColor> colors = null

    List<ManaColor> alliedColors = null

    boolean colorExplicit = false

    @Override
    boolean isMultiColor() {
        colors.size() > 1
    }

    String watermarkName = null

    Rarity rarity = null

    @Override
    String getCopyright() {
        cardSet?.copyright
    }

    @Override
    String getSetTitle() {
        cardSet?.title
    }

    @Override
    String getSetKey() {
        cardSet?.key
    }

    Integer cardNumber = null

    @Override
    Integer getSetCardCount() {
        cardSet?.cards?.size()
    }

}
