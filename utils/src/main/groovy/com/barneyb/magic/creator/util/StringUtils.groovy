package com.barneyb.magic.creator.util

import groovy.transform.TupleConstructor

import java.util.regex.Pattern

/**
 *
 * @author bboisvert
 */
class StringUtils {

    private static final Pattern ESCAPE = ~/(?<!\\)\\(t|u([0-9A-Fa-f]{4}))/

    static String decodeEscapes(String s) {
        def sb = new StringBuilder()
        def m = ESCAPE.matcher(s)
        def any = false
        def nextStart = 0
        while (m.find()) {
            any = true
            if (m.start() > nextStart) {
                sb.append(s.substring(nextStart, m.start()))
            }
            switch (m.group(1).substring(0, 1)) {
                case 't':
                    sb.append("\t")
                    break
                case 'u':
                    sb.append(Character.toString((char) Integer.parseInt(m.group(2), 16)))
                    break
                default:
                    // unrecognized escape
                    sb.append("{").append(m.group()).append("}")
            }
            nextStart = m.end()
        }
        if (! any) {
            return s
        }
        if (nextStart < s.length()) {
            sb.append(s.substring(nextStart))
        }
        sb.toString()
    }

    static interface ParseVisitor {
        void startParagraph()
        void text(String text)
        void lineBreak()
        void symbols(List<String> keys)
        void endParagraph()
    }

    @TupleConstructor
    private static class TextAndSymbolParser {

        @SuppressWarnings("GrFinalVariableAccess")
        final ParseVisitor visitor

        List<String> grp = null

        void parse(List<String> paras) {
            paras.each { para ->
                visitor.startParagraph()
                int open, close, pointer = 0
                while (true) {
                    open = para.indexOf('{', pointer)
                    if (open < pointer) {
                        if (pointer < para.length()) {
                            endGroup()
                            text(para.substring(pointer))
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
                            endGroup()
                            text(para.substring(pointer, open))
                        }
                        symbol(para.substring(open + 1, close))
                        pointer = close + 1
                    }
                }
                endGroup()
                visitor.endParagraph()
            }
        }

        void endGroup() {
            if (grp != null) {
                visitor.symbols(grp)
                grp = null
            }
        }

        void text(String text) {
            endGroup()
            text.readLines().eachWithIndex { String s, int i ->
                if (i > 0) {
                    visitor.lineBreak()
                }
                visitor.text(s)
            }
        }

        void symbol(String key) {
            if (grp == null) {
                grp = [key]
            } else {
                grp << key
            }
        }

    }

    static void parseTextAndSymbols(String raw, ParseVisitor visitor) {
        new TextAndSymbolParser(visitor).parse(toParagraphs(toLines(raw)))
    }

    static String leadingWhitespace(CharSequence s) {
        if (! s.allWhitespace) {
            for (int i = 0; i < s.length(); i++) {
                if (! s.charAt(i).whitespace) {
                    return s.subSequence(0, i).toString()
                }
            }
        }
        s.toString()
    }

    static List<String> toParagraphs(List<String> lines) {
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

    static List<String> toLines(String text) {
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
