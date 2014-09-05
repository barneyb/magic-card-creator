package com.barneyb.magic.creator.theme
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

import java.awt.Font
import java.awt.font.TextAttribute
import java.awt.geom.Dimension2D
import java.awt.geom.Rectangle2D

import static java.awt.font.TextAttribute.*
/**
 * Created by barneyb on 7/27/2014.
 */
@TupleConstructor(includeSuperFields = true)
@EqualsAndHashCode(callSuper = true)
@Canonical
class TextBox extends Rectangle2D.Double {

    String family = 'Comic Sans MS' // this might not work on non-Windows

    boolean bold = false

    Font getFont() {
        new Font(textAttributes)
    }

    Font getItalicFont() {
        // see if we can get the 'italic' face of the same family
        def attrs = textAttributes
        attrs[FAMILY] += ' italic'
        def f = new Font(attrs)
        if (f.family == family) {
            // yay!
            return f
        }
        // nope.  let Java derive one....
        font.deriveFont(Font.ITALIC)
    }

    Map<TextAttribute, ?> getTextAttributes() {
        [
            (FAMILY): family,
            (WEIGHT): bold ? WEIGHT_BOLD : WEIGHT_REGULAR,
        ]
    }

    TextBox minus(Dimension2D dim) {
        new TextBox(x, y, width - dim.width, height - dim.height, family, bold)
    }

}
