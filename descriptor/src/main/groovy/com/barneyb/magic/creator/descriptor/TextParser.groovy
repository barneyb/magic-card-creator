package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.descriptor.schema.RulesTextType
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import com.barneyb.magic.creator.symbol.DefaultSymbolGroup

/**
 *
 *
 * @author barneyb
 */
class TextParser {

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    List<List<BodyItem>> getRulesText(RulesTextType el) {
        // todo
        []
    }

    List<List<BodyItem>> getRulesText(String text) {
        getText(text, DefaultRulesText)
    }

    List<List<BodyItem>> getNonNormativeText(String text) {
        getText(text, DefaultNonNormativeText)
    }

    List<List<BodyItem>> getText(String raw, Class<? extends BodyItem> textClass) {
        toParagraphs(toLines(raw)).collect { para ->
            def rawItems = [] // Strings and Symbols
            int open, close, pointer = 0
            while (true) {
                open = para.indexOf('{', pointer)
                if (open < pointer) {
                    if (pointer < para.length()) {
                        rawItems << para.substring(pointer)
                    }
                    break
                }
                close = para.indexOf('}', open)
                if (close < 0) {
                    // pretend there was one added to the end of the string
                    close = para.length()
                }
                if (open >= 0 && open < close) {
                    if (open > pointer) {
                        rawItems << para.substring(pointer, open)
                    }
                    def symbol = para.substring(open + 1, close)
                    rawItems << symbolFactory.getSymbol(symbol)
                    pointer = close + 1
                }
            }
            def items = []
            // convert line breaks and group symbols
            SymbolGroup grp = null
            rawItems.each {
                if (it instanceof Symbol) {
                    if (grp == null) {
                        grp = new DefaultSymbolGroup([it])
                        items << grp
                    } else {
                        grp << it
                    }
                } else { // String
                    if (grp != null) {
                        grp = null
                    }
                    ((String) it).readLines().eachWithIndex { String s, int i ->
                        if (i > 0) {
                            items << new DefaultLineBreak()
                        }
                        items << textClass.newInstance(s)
                    }
                }
            }
            items
        }
    }

    protected String leadingWhitespace(CharSequence s) {
        if (! s.allWhitespace) {
            for (int i = 0; i < s.length(); i++) {
                if (! s.charAt(i).whitespace) {
                    return s.subSequence(0, i).toString()
                }
            }
        }
        s.toString()
    }

    protected List<String> toParagraphs(List<String> lines) {
        def paras = []
        StringBuilder para = new StringBuilder()
        def lastIndent = ''
        lines.each { line ->
            if (line.allWhitespace) {
                if (para.length() > 0) {
                    paras << para.toString()
                    para = new StringBuilder()
                }
            } else {
                def thisIndent = leadingWhitespace(line)
                if (lastIndent == thisIndent) {
                    if (para.length() > 0) {
                        para.append(' ')
                    }
                    para.append(line.trim())
                } else {
                    if (para.length() > 0) {
                        para.append('\n')
                    }
                    para.append(line)
                }
                lastIndent = thisIndent
            }
        }
        if (para != null) {
            paras << para.toString()
        }
        paras
    }

    protected List<String> toLines(String text) {
        if (text == null || text.allWhitespace) {
            return []
        }
        def lines = text.replaceAll(/\r\n?/, '\n').replaceAll(/\n\n+/, '\n\n').stripIndent().readLines()
        // this is a bunch of inefficient list and string manipulation.  but it's correct!
        while (lines.first().allWhitespace) {
            lines.remove(0)
        }
        while (lines.last().allWhitespace) {
            lines.remove(lines.size() - 1)
        }
        lines.collect {
            if (it.allWhitespace) {
                it = ''
            } else {
                while (it.length() > 0 && it.charAt(it.length() - 1).whitespace) {
                    it = it.substring(0, it.length() - 1)
                }
            }
            it
        }
    }

}
