package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface SpellCard extends Card {

    List<Symbol> getCastingCost()

    boolean isSemiEnchantment()

}