package com.barneyb.magic.creator.compositor

import groovy.transform.Immutable

/**
 *
 * @author bboisvert
 */
@Immutable
class AbilityText implements RenderableText {

    String text

    String toString() {
        text
    }

}
