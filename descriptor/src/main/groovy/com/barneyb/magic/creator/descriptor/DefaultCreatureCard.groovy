package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.CreatureCard

/**
 *
 *
 * @author barneyb
 */
class DefaultCreatureCard extends DefaultCard implements CreatureCard {

    String power = null

    String toughness = null

    @Override
    boolean isLeveler() {
        levels != null && ! levels.empty
    }

    List<CreatureCard.Level> levels = null
}
