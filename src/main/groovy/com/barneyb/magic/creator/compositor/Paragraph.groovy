package com.barneyb.magic.creator.compositor

import groovy.transform.Immutable

/**
 *
 * @author bboisvert
 */
@Immutable
class Paragraph implements Renderable {

    @Override
    String toString() {
        "\u00b6"
    }

}
