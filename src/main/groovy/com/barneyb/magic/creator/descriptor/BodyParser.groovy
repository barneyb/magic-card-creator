package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.compositor.AbilityText
import com.barneyb.magic.creator.compositor.FlavorText
import com.barneyb.magic.creator.compositor.Paragraph
/**
 *
 * @author bboisvert
 */
class BodyParser {

    static List<List> parse(String text) {
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
            [it == '' ? new Paragraph() : new FlavorText(it)]
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
                    items << new AbilityText(text.substring(pointer))
                }
                break
            }
            close = text.indexOf('}', open)
            if (close < 0) {
                // pretend there was one added to the end of the string
                close = text.length()
            }
            if (open >= 0 && open < close) {
                if (open > pointer) {
                    items << new AbilityText(text.substring(pointer, open))
                }
                def symbol = text.substring(open + 1, close)
                if (CostType.isSymbol(symbol)) {
                    items << CostType.fromSymbol(symbol)
                } else {
                    items << new FlavorText(symbol)
                }
                pointer = close + 1
            }
        }
        items
    }

}
