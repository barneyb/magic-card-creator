package com.barneyb.magic.creator.compositor
import groovy.transform.Immutable
/**
 *
 * @author bboisvert
 */
@Immutable
class FlavorText implements RenderableText {

    String text


    String toString() {
        "{$text}"
    }

}
