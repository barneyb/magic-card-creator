package com.barneyb.magic.creator.descriptor.markdown

import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.BodyText
import com.barneyb.magic.creator.api.NonNormativeText
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.core.DefaultCard
import com.barneyb.magic.creator.core.DefaultCardSet
import com.barneyb.magic.creator.core.DefaultCreatureCard
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.core.SimpleArtwork
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import groovy.transform.PackageScope
import org.tautua.markdownpapers.ast.CharRef
import org.tautua.markdownpapers.ast.Code
import org.tautua.markdownpapers.ast.CodeSpan
import org.tautua.markdownpapers.ast.CodeText
import org.tautua.markdownpapers.ast.Comment
import org.tautua.markdownpapers.ast.Document
import org.tautua.markdownpapers.ast.Emphasis
import org.tautua.markdownpapers.ast.EmptyTag
import org.tautua.markdownpapers.ast.EndTag
import org.tautua.markdownpapers.ast.Header
import org.tautua.markdownpapers.ast.Image
import org.tautua.markdownpapers.ast.InlineUrl
import org.tautua.markdownpapers.ast.Item
import org.tautua.markdownpapers.ast.Line
import org.tautua.markdownpapers.ast.LineBreak
import org.tautua.markdownpapers.ast.Link
import org.tautua.markdownpapers.ast.Paragraph
import org.tautua.markdownpapers.ast.Quote
import org.tautua.markdownpapers.ast.ResourceDefinition
import org.tautua.markdownpapers.ast.Ruler
import org.tautua.markdownpapers.ast.SimpleNode
import org.tautua.markdownpapers.ast.StartTag
import org.tautua.markdownpapers.ast.Tag
import org.tautua.markdownpapers.ast.TagAttribute
import org.tautua.markdownpapers.ast.TagAttributeList
import org.tautua.markdownpapers.ast.TagBody
import org.tautua.markdownpapers.ast.Text
import org.tautua.markdownpapers.ast.Visitor

import java.util.regex.Pattern

import static com.barneyb.magic.creator.util.StringUtils.*
/**
 *
 *
 * @author barneyb
 */
@PackageScope
class MarkdownVisitor implements Visitor {

    public static final Pattern COST_PATTERN = ~/^([0-9]|[wugrbWUGRB]|\{([0-9]+|[wugrbWUGRB])\})+$/

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    final URL base

    final DefaultCardSet cardSet

    MarkdownVisitor(URL base, DefaultCardSet cardSet) {
        this.base = base
        this.cardSet = cardSet
    }

    private DefaultCard card

    private StringBuilder buffer
    private Stack<StringBuilder> textStack = []

    protected StringBuilder push() {
        buffer = textStack.push(new StringBuilder())
    }

    protected StringBuilder pop() {
        def b = textStack.pop()
        buffer = textStack.size() == 0 ? null : textStack.peek()
        b
    }

    protected void closeItem() {
        if (textStack.size() == 0) {
            return
        }
        def body = decodeEscapes(pop().toString().trim())
        if (card == null) {
            // must be the set.
            if (! body.allWhitespace) {
                cardSet.copyright = body.replaceAll(/\s+/, ' ')
            }
        } else {
            def lines = toLines(body)*.trim()
            def type = lines.first()
            lines = lines.tail()
            def subtype = null
            def iDash = type.indexOf('-')
            if (iDash > 0) {
                subtype = type.substring(iDash + 1)
                type = type.substring(0, iDash )
            }
            card.typeParts = type?.tokenize()
            if (card.isType("Creature")) {
                // have to transmute...
                cardSet.cards.remove(card)
                card = new DefaultCreatureCard(
                    title: card.title,
                    castingCost: card.castingCost,
                    artwork: card.artwork,
                    typeParts: card.typeParts
                )
                cardSet.cards << card
                if (lines.last().matches(/[0-9Xx*]+\/[0-9Xx*]+/)) {
                    def pt = lines.remove(lines.size() - 1)
                    pt = pt.tokenize('/')
                    card.power = pt.first().trim()
                    card.toughness = pt.last().trim()
                }
            }
            def visitor = new ParseVisitor() {

                List<BodyItem> items
                List<List<BodyItem>> paras

                @Override
                void startParagraph() {
                    if (! paras) {
                        paras = []
                    }
                    items = []
                }

                void splitAndAdd(String text, Class<? extends BodyText> clazz) {
                    text.replace('<br/>', '\5').tokenize('\5').eachWithIndex { it, i ->
                        if (i > 0 || it.allWhitespace) {
                            items << new DefaultLineBreak()
                        }
                        if (! it.allWhitespace) {
                            items << clazz.newInstance(it)
                        }
                    }
                }

                @Override
                void text(String text) {
                    int start = 0, open, close
                    while (true) {
                        open = text.indexOf('<em', start)
                        if (open < 0) {
                            if (start < text.length()) {
                                splitAndAdd(text.substring(start), DefaultRulesText)
                            }
                            break
                        }
                        close = text.indexOf('</em', open)
                        if (close < open) {
                            close = text.length()
                        }
                        if (open > start) {
                            splitAndAdd(text.substring(start, open), DefaultRulesText)
                        }
                        splitAndAdd(text.substring(text.indexOf('>', open) + 1, close), DefaultNonNormativeText)
                        start = text.indexOf('>', close) + 1
                    }
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
                    items = null
                }

            }

            parseTextAndSymbols(toParagraphs(lines), visitor)
            def startedFlavor = false,
                rules = [],
                flavor = []
            visitor.paras.each {
                startedFlavor = startedFlavor || it.first() instanceof NonNormativeText
                if (startedFlavor) {
                    flavor << it
                } else {
                    rules << it
                }
            }
            card.rulesText = rules.size() == 0 ? null : rules
            card.flavorText = flavor.size() == 0 ? null : flavor
            card.subtypeParts = subtype?.tokenize()
        }
    }

    protected void openItem(String text, int level) {
        text = text.trim()
        if (level == 1) {
            // cardset name
            cardSet.title = decodeEscapes(text.trim())
            cardSet.key = cardSet.title.tokenize().collect {
                it.charAt(0)
            }.findAll {
                Character.isLetter(it)
            }.join('')
        } else if (level == 2) {
            // card title and trailing casting cost
            int i = text.lastIndexOf(' ')
            def cost = text.substring(i + 1)
            if (cost.matches(COST_PATTERN)) {
                card = new DefaultCard(
                    title: decodeEscapes(text.substring(0, i)),
                    castingCost: symbolFactory.getCost(cost)
                )
            } else {
                card = new DefaultCard(
                    title: decodeEscapes(text)
                )
            }
            cardSet.cards << card
        }
        push()
    }

    @Override
    void visit(Document node) {
        doKids(node)
        closeItem()
    }

    @Override
    void visit(Emphasis node) {
        buffer.append('<em>')
        doKids(node)
        buffer.append('</em>')
    }

    @Override
    void visit(Header node) {
        closeItem()
        push()
        doKids(node)
        openItem(pop().toString(), node.level)
    }

    @Override
    void visit(Image node) {
        if (card != null) {
            card.artwork = new SimpleArtwork(
                new URL(base, node.resource.location),
                node.text
            )
        }
    }

    @Override
    void visit(Line node) {
        doKids(node)
        buffer.append('\n')
    }

    @Override
    void visit(Paragraph node) {
        doKids(node)
        buffer.append('\n')
    }

    @Override
    void visit(Text node) {
        buffer.append(node.value)
    }

    @Override
    void visit(EmptyTag node) {
        if (node.name == 'br') {
            buffer.append("<br/>")
        }
    }

    protected void doKids(org.tautua.markdownpapers.ast.Node node){
        int count = node.jjtGetNumChildren()
        for(int i = 0; i < count; i++) {
            node.jjtGetChild(i).accept(this)
        }
    }

    @Override void visit(SimpleNode node) { doKids(node) }
    @Override void visit(ResourceDefinition node) { doKids(node)}
    @Override void visit(InlineUrl node) { doKids(node) }
    @Override void visit(StartTag node) { doKids(node) }
    @Override void visit(Tag node) { doKids(node) }
    @Override void visit(TagAttribute node) { doKids(node) }
    @Override void visit(TagAttributeList node) { doKids(node) }
    @Override void visit(TagBody node) { doKids(node) }
    @Override void visit(Ruler node) { doKids(node) }
    @Override void visit(Quote node) { doKids(node) }
    @Override void visit(Item node) { doKids(node) }
    @Override void visit(LineBreak node) { doKids(node) }
    @Override void visit(Link node) { doKids(node) }
    @Override void visit(EndTag node) { doKids(node) }
    @Override void visit(CharRef node) { doKids(node) }
    @Override void visit(Code node) { doKids(node) }
    @Override void visit(CodeSpan node) { doKids(node) }
    @Override void visit(CodeText node) { doKids(node) }
    @Override void visit(Comment node) { doKids(node) }
    @Override void visit(org.tautua.markdownpapers.ast.List node) { doKids(node) }
}
