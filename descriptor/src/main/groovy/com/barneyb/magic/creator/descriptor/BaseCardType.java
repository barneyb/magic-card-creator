package com.barneyb.magic.creator.descriptor;

import com.barneyb.magic.creator.descriptor.schema.*;

import java.util.List;

/**
 * @author barneyb
 */
public abstract class BaseCardType {

    public ArtworkType getArtwork() {
        throw new UnsupportedOperationException("This card doesn't have artwork");
    }

    public ArtworkType getOverArtwork() {
        throw new UnsupportedOperationException("This card doesn't have over artwork");
    }

    public String getColorIndicator() {
        throw new UnsupportedOperationException("This card doesn't have a color indicator");
    }

    public String getSubtype() {
        throw new UnsupportedOperationException("This card doesn't have a subtype");
    }

    public RarityEnum getRarity() {
        throw new UnsupportedOperationException("This card doesn't have a rarity");
    }

    public RulesTextType getRulesText() {
        throw new UnsupportedOperationException("This card doesn't have rules text");
    }

    public NonNormativeTextType getFlavorText() {
        throw new UnsupportedOperationException("This card doesn't have flavor text");
    }

    public String getWatermark() {
        throw new UnsupportedOperationException("This card doesn't have a watermark");
    }

    public String getTitle() {
        throw new UnsupportedOperationException("This card doesn't have a title");
    }

    public String getCastingCost() {
        throw new UnsupportedOperationException("This card doesn't have a casting cost");
    }

    public String getAlliedColors() {
        throw new UnsupportedOperationException("This card doesn't have type modifiers");
    }

    public String getType() {
        throw new UnsupportedOperationException("This card doesn't have type");
    }

    public String getTypeModifiers() {
        throw new UnsupportedOperationException("This card doesn't have type modifiers");
    }

    public String getPower() {
        throw new UnsupportedOperationException("This card doesn't have power");
    }

    public String getToughness() {
        throw new UnsupportedOperationException("This card doesn't have toughness");
    }

    public List<LevelType> getLevels() {
        throw new UnsupportedOperationException("This card doesn't have levels");
    }

    public String getLoyalty() {
        throw new UnsupportedOperationException("This card doesn't have loyalty");
    }

    public List<LoyaltyAbilityType> getLoyaltyAbilities() {
        throw new UnsupportedOperationException("This card doesn't have loyalty abilities");
    }

}
