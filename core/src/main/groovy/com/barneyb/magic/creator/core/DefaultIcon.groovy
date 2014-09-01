package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Icon
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import org.w3c.dom.svg.SVGDocument

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode(includes = "key")
class DefaultIcon implements Icon {

    String key

    SVGDocument document

    @Override
    Dimension getSize() {
        new DoubleDimension(
            document.rootElement.width.baseVal.value,
            document.rootElement.height.baseVal.value
        )
    }

}
