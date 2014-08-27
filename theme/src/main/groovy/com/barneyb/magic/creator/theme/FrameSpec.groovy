package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.LayoutType

/**
 *
 *
 * @author barneyb
 */
class FrameSpec {

    protected LayoutType type
    protected Card card

    def FrameSpec(LayoutType type, Card card) {
        this.type = type
        this.card = card
    }

    boolean isCreature() {
        type == LayoutType.CREATURE
    }

}
