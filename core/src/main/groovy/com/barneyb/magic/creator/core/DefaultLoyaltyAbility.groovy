package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.LoyaltyAbility
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DefaultLoyaltyAbility implements LoyaltyAbility {

    String cost = null

    List<BodyItem> rulesText = null

}
