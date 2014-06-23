package com.barneyb.magic.creator.compositor
import groovy.transform.Immutable
/**
 *
 * @author bboisvert
 */
@Immutable
class RenderableString implements Renderable {

    String text

    boolean flavor

    String toString() {
        flavor ? "{$text}" : text
    }

}
