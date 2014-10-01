package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.RulesText
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.descriptor.schema.NonNormativeTextType
import com.barneyb.magic.creator.descriptor.schema.RulesTextType
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import com.barneyb.magic.creator.util.StringUtils

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
        def vis = new StringUtils.ParseVisitor() {

            List<BodyItem> items
            List<List<BodyItem>> paras = []

            @Override
            void startParagraph() {
                items = []
            }

            @Override
            void text(String text) {
                items << textClass.newInstance(text)
            }

            @Override
            void lineBreak() {
                items << new DefaultLineBreak()
            }

            @Override
            void symbols(List<String> keys) {
                items << symbolFactory.getCost(keys)
            }

            @Override
            void endParagraph() {
                if (items && items.size() > 0) {
                    paras << items
                }
            }

        }
        StringUtils.parseTextAndSymbols(raw, vis)
        vis.paras.size() == 0 ? null : vis.paras
    }

}
