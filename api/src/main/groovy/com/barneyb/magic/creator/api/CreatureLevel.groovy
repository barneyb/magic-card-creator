package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface CreatureLevel {

    /**
     * The label for this level or range of levels.
     */
    String getLabel()

    String getPower()

    String getToughness()

    List<List<BodyItem>> getRulesText()

}
