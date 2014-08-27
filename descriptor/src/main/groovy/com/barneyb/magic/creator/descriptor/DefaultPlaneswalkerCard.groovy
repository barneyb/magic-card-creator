package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.PlaneswalkerCard

/**
 *
 *
 * @author barneyb
 */
class DefaultPlaneswalkerCard extends DefaultCard implements PlaneswalkerCard {

    String loyalty = null

    List<PlaneswalkerCard.LoyaltyAbility> loyaltyAbilities = null

}
