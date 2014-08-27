package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.descriptor.schema.LevelType
import com.barneyb.magic.creator.descriptor.schema.LoyaltyAbilityType

/**
 *
 *
 * @author barneyb
 */
abstract class BaseCardType implements CardType {

    String getCastingCost() {
        throw new UnsupportedOperationException("This card doesn't have a casting cost")
    }

    String getType() {
        throw new UnsupportedOperationException("This card doesn't have type")
    }

    String getTypeModifiers() {
        throw new UnsupportedOperationException("This card doesn't have type modifiers")
    }

    String getPower() {
        throw new UnsupportedOperationException("This card doesn't have power")
    }

    String getToughness() {
        throw new UnsupportedOperationException("This card doesn't have toughness")
    }

    List<LevelType> getLevels() {
        throw new UnsupportedOperationException("This card doesn't have levels")
    }

    String getLoyalty() {
        throw new UnsupportedOperationException("This card doesn't have loyalty")
    }

    List<LoyaltyAbilityType> getLoyaltyAbilities() {
        throw new UnsupportedOperationException("This card doesn't have loyalty abilities")
    }

}
