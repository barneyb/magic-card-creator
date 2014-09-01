package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
/**
 *
 *
 * @author barneyb
 */
class DefaultFusedCard extends DefaultCard {

    @Override
    LayoutType getLayoutType() {
        LayoutType.FUSE
    }

    @Override
    String getTitle() {
        fusedCards*.title.join(' // ')
    }

    @Override
    List<Symbol> getCastingCost() {
        throw new UnsupportedOperationException("Fused cards don't have casting costs, their components do.")
    }

    @Override
    Artwork getArtwork() {
        throw new UnsupportedOperationException("Fused cards don't have artwork, their components do.")
    }

    @Override
    Artwork getOverArtwork() {
        throw new UnsupportedOperationException("Fused cards don't have artwork, their components do.")
    }

    @Override
    List<String> getTypeParts() {
        throw new UnsupportedOperationException("Fused cards don't have types, their components do.")
    }

    @Override
    List<String> getSubtypeParts() {
        throw new UnsupportedOperationException("Fused cards don't have types, their components do.")
    }

    @Override
    boolean isSemiEnchantment() {
        throw new UnsupportedOperationException("Fused cards don't have types, their components do.")
    }

    @Override
    List<List<BodyItem>> getRulesText() {
        throw new UnsupportedOperationException("Fused cards don't have text, their components do.")
    }

    @Override
    List<List<BodyItem>> getFlavorText() {
        throw new UnsupportedOperationException("Fused cards don't have text, their components do.")
    }

    @Override
    List<ManaColor> getColors() {
        throw new UnsupportedOperationException("Fused cards don't have colors, their components do.")
    }

    @Override
    boolean isColorExplicit() {
        throw new UnsupportedOperationException("Fused cards don't have colors, their components do.")
    }

    @Override
    boolean isMultiColor() {
        throw new UnsupportedOperationException("Fused cards don't have colors, their components do.")
    }

    @Override
    List<ManaColor> getAlliedColors() {
        throw new UnsupportedOperationException("Fused cards don't have colors, their components do.")
    }

    @Override
    String getWatermarkName() {
        throw new UnsupportedOperationException("Fused cards don't have watermarks, their components do.")
    }

}
