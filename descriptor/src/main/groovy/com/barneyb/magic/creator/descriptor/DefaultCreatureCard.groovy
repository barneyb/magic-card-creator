package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.CreatureLevel

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

    List<CreatureLevel> levels = null
}
