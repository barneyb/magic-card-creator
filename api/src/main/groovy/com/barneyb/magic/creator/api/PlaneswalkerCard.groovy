package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface PlaneswalkerCard extends Card {

    String getLoyalty()

    List<LoyaltyAbility> getLoyaltyAbilities()

}
