package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.LoyaltyAbility
import com.barneyb.magic.creator.api.PlaneswalkerCard

/**
 *
 *
 * @author barneyb
 */
class DefaultPlaneswalkerCard extends DefaultCard implements PlaneswalkerCard {

    @Override
    LayoutType getLayoutType() {
        LayoutType.PLANESWALKER
    }

    String loyalty = null

    List<LoyaltyAbility> loyaltyAbilities = null

}
