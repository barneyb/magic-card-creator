package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.*

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

    boolean colorExplicit = false

    @Override
    boolean isMultiColor() {
        (colors - ManaColor.COLORLESS).size() > 0
    }

    String watermarkName = null

    Rarity rarity = null

    Locale locale = Locale.default

    @Override
    String getCopyright() {
        cardSet?.copyright
    }

    @Override
    String getSetTitle() {
        cardSet?.title
    }

    Integer cardNumber = null

    @Override
    Integer getSetCardCount() {
        cardSet?.cards?.size()
    }

}
