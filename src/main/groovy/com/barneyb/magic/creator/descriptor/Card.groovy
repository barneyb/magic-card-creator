package com.barneyb.magic.creator.descriptor

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
@TupleConstructor
@EqualsAndHashCode(includes = ["title"])
class Card {

    String title

    String costString

    List<CostType> getCost() {
        CostParser.parse(costString)
    }

    Collection<ManaColor> getColors() {
        (cost*.colors.flatten().unique() - ManaColor.COLORLESS).sort()
    }

    String artwork

    String type

    boolean isCreature() {
        type.toLowerCase().tokenize().contains("creature")
    }

    String subtype

    String power

    String toughness

    String abilities

    String flavor

    String artist

}
