package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.descriptor.schema.Level
import com.barneyb.magic.creator.descriptor.schema.LoyaltyAbility
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

    List<Level> getLevels() {
        throw new UnsupportedOperationException("This card doesn't have levels")
    }

    String getLoyalty() {
        throw new UnsupportedOperationException("This card doesn't have loyalty")
    }

    List<LoyaltyAbility> getLoyaltyAbilities() {
        throw new UnsupportedOperationException("This card doesn't have loyalty abilities")
    }

}
