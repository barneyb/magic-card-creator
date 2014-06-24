package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.compositor.Paragraph
import com.barneyb.magic.creator.compositor.RenderableString
/**
 *
 * @author bboisvert
 */
class BodyParser {

    static List<List> parseAbilities(String text) {
        toLines(text).collect {
            if (it == '') {
               [new Paragraph()]
            } else {
                toItems(it)
            }
        }
    }

    static List<List> parseFlavor(String text) {
        toLines(text).collect {
            [it == '' ? new Paragraph() : new RenderableString(it, true)]
        }
    }

    static protected List<String> toLines(String text) {
        text.trim().replace('\r', '').replaceAll(/\n\n+/, '\n\n').readLines()
    }

    static protected List toItems(String text) {
        def items = []
        int open, close, pointer = 0
        while (true) {
            open = text.indexOf('{', pointer)
            if (open < pointer) {
                if (pointer < text.length()) {
                    items << new RenderableString(text.substring(pointer), false)
                }
                break
            }
            close = text.indexOf('}', open)
            if (open >= 0 && open < close) {
                if (open > pointer) {
                    items << new RenderableString(text.substring(pointer, open), false)
                }
                def symbol = text.substring(open + 1, close)
                if (CostType.isSymbol(symbol)) {
                    items << CostType.fromSymbol(symbol)
                } else {
                    items << new RenderableString(symbol, true)
                }
                pointer = close + 1
            }
        }
        items
    }

}
