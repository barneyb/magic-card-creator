package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.RulesText
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.core.DefaultSymbolGroup
import com.barneyb.magic.creator.descriptor.schema.NonNormativeTextType
import com.barneyb.magic.creator.descriptor.schema.RulesTextType
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory

import javax.xml.bind.JAXBElement

/**
 *
 *
 * @author barneyb
 */
class TextParser {

    static final String LINE_BREAK = '\u23a5'
    static final String OPEN_REMINDER = '\u23a3'
    static final String CLOSE_REMINDER = '\u23a6'

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    List<List<BodyItem>> getNonNormativeText(NonNormativeTextType el) {
        getText(el?.content, DefaultNonNormativeText)
    }

    protected List<List<BodyItem>> getText(List<Serializable> content, Class<? extends BodyItem> textClass) {
        def combined = new StringBuilder()
        def work
        work = {
            if (it instanceof String) {
                combined.append(it)
            } else if (it instanceof JAXBElement) {
                if (it.name.localPart == 'reminder') {
                    combined.append(OPEN_REMINDER)
                    ((NonNormativeTextType) it.value).content.each work
                    combined.append(CLOSE_REMINDER)
                } else if (it.name.localPart == 'br') {
                    combined.append(LINE_BREAK)
                } else {
                    throw new IllegalArgumentException("Unrecognized element: $it.name")
                }
            } else {
                throw new IllegalArgumentException("Unrecognized element: $it")
            }
        }
        content?.each work
        getText(combined.replaceAll(LINE_BREAK + /(\n\s*)/, LINE_BREAK).toString(), textClass)?.collect { para ->
            def items = []
            para.each {
                if (textClass.isAssignableFrom(it.getClass()) && it.text.contains(LINE_BREAK)) {
                    it.text.tokenize(LINE_BREAK).eachWithIndex { s, i ->
                        if (i > 0) {
                            items << new DefaultLineBreak()
                        }
                        items << textClass.newInstance(s)
                    }
                } else {
                    items << it
                }
            }
            items
        }
    }

    List<List<BodyItem>> getRulesText(RulesTextType el) {
        def inReminder = false
        getText(el?.content, DefaultRulesText)?.collect { para ->
            def items = []
            para.each {
                if (it instanceof RulesText) {
                    def rem = inReminder
                    def s = new StringBuilder(it.text)
                    List<String> parts = []
                    while (true) {
                        if (rem) {
                            def close = s.indexOf(CLOSE_REMINDER)
                            if (close < 0) {
                                parts << s.toString()
                                break
                            } else {
                                parts << s.substring(0, close)
                                s.delete(0, close + 1)
                                rem = false
                            }
                        } else {
                            def open = s.indexOf(OPEN_REMINDER)
                            if (open < 0) {
                                parts << s.toString()
                                break
                            } else {
                                parts << s.substring(0, open)
                                s.delete(0, open + 1)
                                rem = true
                            }
                        }
                    }
                    def l = parts.size() - 1
                    parts.eachWithIndex { p, i ->
                        if (p.length() > 0) {
                            //noinspection GroovyConditionalWithIdenticalBranches
                            items << (inReminder ? new DefaultNonNormativeText(p) : new DefaultRulesText(p))
                        }
                        if (i < l) {
                            inReminder = ! inReminder
                        }
                    }
                } else {
                    items << it
                }
            }
            items
        }
    }

    protected List<List<BodyItem>> getText(String raw, Class<? extends BodyItem> textClass) {
        def paras = toParagraphs(toLines(raw)).collect { para ->
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
                        //noinspection GrReassignedInClosureLocalVar
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
        }.findAll {
            it.size() > 0
        }
        paras.size() == 0 ? null : paras
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
