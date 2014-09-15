package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.descriptor.CardSetImporter
import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter

/**
 *
 *
 * @author barneyb
 */
abstract class BaseDescriptorCommand {

    @Parameter(names = ["-d", "--descriptor"], description = "Cardset descriptor", converter = FileConverter, required = true)
    File descriptor

    @Parameter(names = "--cards", description = "Card names or numbers to process", variableArity = true)
    List<String> cards = []

    protected CardSet loadDescriptor() {
        new CardSetImporter().fromFile(descriptor)
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
