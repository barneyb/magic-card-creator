package com.barneyb.magic.creator.descriptor;

import com.barneyb.magic.creator.descriptor.schema.*;

import java.util.List;

/**
 * @author barneyb
 */
public abstract class BaseCardType {

    public abstract ArtworkType getArtwork();

    public abstract ArtworkType getOverArtwork();

    public abstract String getColorIndicator();

    public abstract String getSubtype();

    public abstract RarityEnum getRarity();

    public abstract RulesTextType getRulesText();

    public abstract NonNormativeTextType getFlavorText();

    public abstract String getWatermark();

    public abstract String getTitle();

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
