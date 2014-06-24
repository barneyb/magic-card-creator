package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.compositor.Paragraph
import com.barneyb.magic.creator.compositor.Renderable
import com.barneyb.magic.creator.compositor.RenderableString

/**
 *
 * @author bboisvert
 */
class BodyParser {

    static List<List<Renderable>> parseAbilities(String text) {
        toLines(text).collect {
            if (it == '') {
               [new Paragraph()]
            } else {
                toItems(it)
            }
        }
    }

    static List<List<Renderable>> parseFlavor(String text) {
        toLines(text).collect {
            [it == '' ? new Paragraph() : new RenderableString(it, true)]
        }
    }

    static protected List<String> toLines(String text) {
        text.trim().replace('\r', '').replaceAll(/\n\n+/, '\n\n').readLines()
    }

    static protected List<Renderable> toItems(String text) {
        return [new RenderableString(text, false)]
    }

}
