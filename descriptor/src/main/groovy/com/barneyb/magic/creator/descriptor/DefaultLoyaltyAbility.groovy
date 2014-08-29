package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.PlaneswalkerCard
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DefaultLoyaltyAbility implements PlaneswalkerCard.LoyaltyAbility {

    String cost = null

    List<List<BodyItem>> rulesText = null

}
