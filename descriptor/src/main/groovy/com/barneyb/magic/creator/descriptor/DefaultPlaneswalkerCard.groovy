package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.LoyaltyAbility
import com.barneyb.magic.creator.api.PlaneswalkerCard

/**
 *
 *
 * @author barneyb
 */
class DefaultPlaneswalkerCard extends DefaultCard implements PlaneswalkerCard {

    String loyalty = null

    List<LoyaltyAbility> loyaltyAbilities = null

}
