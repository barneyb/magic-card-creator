package com.barneyb.magic.creator.descriptor
import groovy.transform.TupleConstructor
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
import org.tautua.markdownpapers.parser.Parser

/**
 *
 * @author bboisvert
 */
@TupleConstructor
class MarkdownDescriptor implements CardSetDescriptor, Visitor {

    def MarkdownDescriptor(URL src) {
        this(src, src.newReader())
    }

    def MarkdownDescriptor(URL base, InputStream src, String encoding="utf-8") {
        this(base, new InputStreamReader(src, encoding))
    }

    def MarkdownDescriptor(URL base, String src) {
        this(base, new StringReader(src))
    }

    def MarkdownDescriptor(URL base, Reader src) {
        this.base = base
        this.cardSet = new CardSet()
        if (! (src instanceof BufferedReader)) {
            src = new BufferedReader(src)
        }
        new Parser(src).parse().accept(this)
    }

    final URL base

    final CardSet cardSet

    private Card card

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
        def body = pop().toString().trim()
        if (card == null) {
            // must be the set.
            if (! body.allWhitespace) {
                cardSet.footer = body.replaceAll(/\s+/, ' ')
            }
        } else {
            def type = body.substring(0, body.indexOf('\n')).trim()
            card.body = body.substring(type.length())
            def pt = type.matches(/.* [0-9]+\/[0-9]+/) ? type.substring(type.lastIndexOf(' ')) : null
            if (pt != null) {
                type = type.substring(0, type.length() - pt.length())
                pt = pt.tokenize('/')
                card.power = pt.first()
                card.toughness = pt.last()
            }
            def subtype = null
            def iDash = type.indexOf('-')
            if (iDash > 0) {
                subtype = type.substring(iDash + 1)
                type = type.substring(0, iDash )
            }
            card.type = type
            card.subtype = subtype
        }
    }

    protected void openItem(String text, int level) {
        text = text.trim()
        if (level == 1) {
            // cardset name
            cardSet.name = text.trim()
        } else if (level == 2) {
            // card title and trailing casting cost
            int i = text.lastIndexOf(' ')
            card = new Card(text.substring(0, i), text.substring(i + 1))
            cardSet << card
        }
        push()
    }

    protected void gotImage(String url, String text) {
        if (card == null) {
            return
        }
        card.artwork = new URL(base, url).toString()
        card.artist = text
    }

    @Override
    void visit(Document node) {
        doKids(node)
        closeItem()
    }

    @Override
    void visit(Emphasis node) {
        buffer.append('{')
        doKids(node)
        buffer.append('}')
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
        gotImage(node.resource.location, node.text)
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
    @Override void visit(EmptyTag node) { doKids(node) }
    @Override void visit(EndTag node) { doKids(node) }
    @Override void visit(CharRef node) { doKids(node) }
    @Override void visit(Code node) { doKids(node) }
    @Override void visit(CodeSpan node) { doKids(node) }
    @Override void visit(CodeText node) { doKids(node) }
    @Override void visit(Comment node) { doKids(node) }
    @Override void visit(org.tautua.markdownpapers.ast.List node) { doKids(node) }

}
