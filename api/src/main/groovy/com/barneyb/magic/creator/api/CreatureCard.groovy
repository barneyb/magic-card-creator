package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface CreatureCard extends Card {

    String getPower()

    String getToughness()

    boolean isLeveler()

    List<CreatureLevel> getLevels()

}
