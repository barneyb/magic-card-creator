package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.util.DoubleRectangle
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

import java.awt.Font
import java.awt.font.TextAttribute

import static java.awt.font.TextAttribute.*
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
@EqualsAndHashCode
@Canonical
class TextRegionSpec {

    DoubleRectangle bounds

    String family = 'Comic Sans MS' // this might not work on non-Windows

    boolean bold = false

    def TextRegionSpec(double x, double y, double w, double h, String family=null, boolean bold=false) {
        bounds = new DoubleRectangle(x, y, w, h)
        if (family != null) {
            this.family = family
        }
        this.bold = bold
    }

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

}
