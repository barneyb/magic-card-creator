package com.barneyb.magic.creator.descriptor.markdown

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CardSetReader
import com.barneyb.magic.creator.core.DefaultCardSet
import org.tautua.markdownpapers.parser.Parser

/**
 *
 *
 * @author barneyb
 */
class MarkdownCardSetReader implements CardSetReader {

    final URL base
    final Reader reader

    def MarkdownCardSetReader(URL url) {
        this(url, url.newReader())
    }

    def MarkdownCardSetReader(URI uri) {
        this(uri.toURL())
    }

    def MarkdownCardSetReader(File file) {
        this(file.toURI().toURL(), file.newReader())
    }

    def MarkdownCardSetReader(URL base, InputStream inputStream) {
        this(base, new InputStreamReader(inputStream))
    }

    def MarkdownCardSetReader(URL base, Reader reader) {
        this.base = base
        this.reader = reader
    }

    @Override
    CardSet read() {
        def cs = new DefaultCardSet()
        new Parser(reader).parse().accept(new MarkdownVisitor(base, cs))
        cs.cards.eachWithIndex { Card c, int i ->
            c.cardNumber = i + 1
        }
        cs
    }

    @Override
    void close() throws IOException {
        reader?.close()
    }

}
