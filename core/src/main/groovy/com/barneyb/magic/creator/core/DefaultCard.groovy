package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.SymbolGroup
/**
 *
 *
 * @author barneyb
 */
class DefaultCard implements Card {

    CardSet set

    String title

    SymbolGroup castingCost = null
    void setCastingCost(SymbolGroup castingCost) {
        this.castingCost = castingCost
        if (! colorExplicit) {
            if (castingCost == null || castingCost.empty) {
                colors = [ManaColor.COLORLESS]
            } else {
                colors = castingCost*.colors.flatten().unique()
                if (colors.size() > 1 && colors.contains(ManaColor.COLORLESS)) {
                    colors -= ManaColor.COLORLESS
                }
            }
        }
    }

    Artwork artwork = null

    Artwork overArtwork = null

    List<String> typeParts = null

    @Override
    boolean isType(String type) {
        typeParts != null && typeParts*.toLowerCase().contains(type.toLowerCase())
    }

    List<String> subtypeParts = null

    @Override
    boolean isSubtype(String subtype) {
        subtypeParts != null && subtypeParts*.toLowerCase().contains(subtype.toLowerCase())
    }

    @Override
    LayoutType getLayoutType() {
        isType("land") ? LayoutType.LAND : LayoutType.SPELL
    }

    @Override
    boolean isSemiEnchantment() {
        isType("enchantment") &&
            (isType("creature") ||
                isType("artifact")
            )
    }

    List<List<BodyItem>> rulesText = null

    List<List<BodyItem>> flavorText = null

    List<ManaColor> colors = [ManaColor.COLORLESS]

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
        colors && colors.size() > 1
    }

    String watermarkName = null

    Rarity rarity = null

    boolean isFused() {
        false
    }

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
