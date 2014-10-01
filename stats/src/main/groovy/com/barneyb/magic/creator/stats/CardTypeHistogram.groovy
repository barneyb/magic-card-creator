package com.barneyb.magic.creator.stats
import com.barneyb.magic.creator.api.CardSet
/**
 *
 *
 * @author barneyb
 */
class CardTypeHistogram extends BaseHistogram {

    static final List<String> TYPES = [
        'Land',
        'Instant',
        'Sorcery',
        'Enchantment',
        'Artifact',
        'Creature',
        'Planeswalker'
    ]

    CardTypeHistogram(CardSet cs) {
        super((TYPES.collectEntries {
            [it, 0]
        } + cs.cards*.typeParts.flatten().countBy { it }).findAll { t, n ->
            TYPES.contains(t)
        })
    }

}
