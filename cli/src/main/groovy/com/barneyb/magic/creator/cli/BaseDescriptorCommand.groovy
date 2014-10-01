package com.barneyb.magic.creator.cli
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CardSetReader
import com.barneyb.magic.creator.descriptor.XmlCardSetReader
import com.barneyb.magic.creator.descriptor.markdown.MarkdownCardSetReader
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import groovy.transform.TupleConstructor
/**
 *
 *
 * @author barneyb
 */
abstract class BaseDescriptorCommand {

    @TupleConstructor
    static enum Format {
        xml(XmlCardSetReader),
        md(MarkdownCardSetReader)

        @SuppressWarnings("GrFinalVariableAccess")
        final Class<? extends CardSetReader> readerClass
    }

    @Parameter(names = ["-d", "--descriptor"], description = "Cardset descriptor", required = true)
    URL descriptor

    @Parameter(names = "--descriptor-format", description = "The format of the descriptor (xml or md), defaults based on file extension")
    Format descriptorFormat = null

    @Parameter(names = "--cards", description = "Card names or numbers to process", variableArity = true)
    List<String> cards = []

    protected CardSet loadDescriptor() {
        if (descriptorFormat == null) {
            def ext = descriptor.path.tokenize('.').last()
            try {
                descriptorFormat = Format.valueOf(ext)
            } catch (IllegalArgumentException ignored) {
                throw new ParameterException("No '$ext' descriptor format is known.")
            }
        }
        descriptorFormat.readerClass.newInstance(descriptor).read()
    }

    protected List<Card> filterCards(CardSet cs) {
        if (cards == null || cards.size() == 0) {
            return cs.cards
        }
        def uniquer = new IdentityHashMap()
        def accept = []
        cards.each { str ->
            Card c
            if (str.isInteger()) {
                def i = str as int
                if (i <= cs.cards.size()) {
                    c = cs.cards.get(i - 1) // one-based indexing on the command line
                }
            } else { // treat as string
                c = cs.cards.find {
                    str.equalsIgnoreCase(it.title)
                }
            }
            if (c != null && uniquer.put(c, true) == null) {
                accept << c
            }
        }
        accept
    }

}
