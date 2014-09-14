package com.barneyb.magic.creator.theme
import java.awt.Font
import java.awt.font.TextAttribute

import static java.awt.font.TextAttribute.*
/**
 *
 *
 * @author barneyb
 */
class FontSpec {

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

}
