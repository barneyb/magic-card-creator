package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface LoyaltyAbility {

    /**
     * The change in loyalty this ability causes.
     */
    String getCost()

    List<List<BodyItem>> getRulesText()

}
