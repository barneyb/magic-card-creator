package com.barneyb.magic.creator.theme

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.Layout
import com.barneyb.magic.creator.api.LayoutType
import org.w3c.dom.svg.SVGDocument

/**
 *
 *
 * @author barneyb
 */
class DefaultLayout implements Layout {

    final LayoutType type

    def DefaultLayout(LayoutType type) {
        this.type = type
    }

    @Override
    SVGDocument layout(Card card) {
        return null
    }

}
