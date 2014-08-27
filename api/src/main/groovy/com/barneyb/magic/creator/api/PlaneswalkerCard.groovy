package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface PlaneswalkerCard extends Card {

    static interface LoyaltyAbility {

        /**
         * The change in loyalty this ability causes.
         */
        String getCost()

        List<List<BodyItem>> getRulesText()

    }

    String getLoyalty()

    List<LoyaltyAbility> getLoyaltyAbilities()

}
