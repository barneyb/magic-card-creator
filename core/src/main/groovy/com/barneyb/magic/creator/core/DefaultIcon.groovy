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
    URL __url
    String __documentString
    SVGDocument __document

    def DefaultIcon(String key, URL url) {
        this.key = key
        this.__url = url
    }

    def DefaultIcon(String key, SVGDocument doc) {
        this.key = key
        this.__document = doc
    }

    def DefaultIcon(String key, String doc) {
        this.key = key
        this.__documentString = doc
    }

    private String needString() {
        if (__documentString == null) {
            __documentString = __url.text
        }
        __documentString
    }

    SVGDocument getDocument() {
        if (__document == null) {
            __document = XmlUtils.read(needString())
        }
        __document
    }

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
