package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.CreatureCard
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class DefaultLevel implements CreatureCard.Level {

    String label = null

    String power = null

    String toughness = null

    List<List<BodyItem>> rulesText = null

}
