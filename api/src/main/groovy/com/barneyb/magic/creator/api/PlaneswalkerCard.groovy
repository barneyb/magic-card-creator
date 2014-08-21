package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface PlaneswalkerCard extends SpellCard {

    static interface LoyaltyAbility {

        /**
         * The change in loyalty this ability causes.
         */
        String getCost()

        List<List<BodyItem>> getBody()

    }

    String getLoyalty()

    List<LoyaltyAbility> getLoyaltyAbilities()

}
