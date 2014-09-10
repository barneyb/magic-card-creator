package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.CreatureLevel
import com.barneyb.magic.creator.api.LayoutType

/**
 *
 *
 * @author barneyb
 */
class DefaultCreatureCard extends DefaultCard implements CreatureCard {

    @Override
    LayoutType getLayoutType() {
        leveler ? LayoutType.LEVELER : LayoutType.CREATURE
    }

    String power = null

    String toughness = null

    @Override
    boolean isLeveler() {
        levels != null && ! levels.empty
    }

    List<CreatureLevel> levels = null
}
