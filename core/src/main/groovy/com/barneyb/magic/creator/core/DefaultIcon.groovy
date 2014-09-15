package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.util.SvgUtils
import com.barneyb.magic.creator.util.XmlUtils
import groovy.transform.EqualsAndHashCode
import org.w3c.dom.svg.SVGDocument

import java.awt.geom.Dimension2D
/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(includes = "key")
class DefaultIcon implements Icon {

    String key

    def DefaultIcon(String key, SVGDocument doc) {
        this.key = key
        this.__document = doc
    }

    def DefaultIcon(String key, String doc) {
        this.key = key
        this.__documentString = doc
    }

    SVGDocument __document
    SVGDocument getDocument() {
        if (__document == null) {
            __document = XmlUtils.read(__documentString)
        }
        __document
    }

    String __documentString
    String getDocumentString() {
        if (__documentString == null) {
            __documentString =  XmlUtils.write(__document)
        }
        __documentString
    }

    @Override
    Dimension2D getSize() {
        SvgUtils.size(document)
    }

}
