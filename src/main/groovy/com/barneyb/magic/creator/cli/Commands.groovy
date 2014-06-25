package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.awt.AwtCompositor
import com.barneyb.magic.creator.descriptor.CardSet
import com.barneyb.magic.creator.descriptor.CardValidator
import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
@TupleConstructor
enum Commands {

    HELP({ msg=null ->
        if (msg != null) {
            println msg
        }
        println "The following subcommands are available:"
        Commands.enumConstants.each {
            println "  " + it.name().toLowerCase()
        }
        System.exit(1)
    }),

    VALIDATE({ CardSet cards ->
        println "Validating '$cards.name':"
        def v = new CardValidator()
        int invalids = 0
        cards.each { card ->
            def es = v.validate(card)
            if (es.size() > 0) {
                invalids += 1
                println "#$card.cardOfSet $card.title"
                es.each {
                    println "  $it"
                }
            }
        }
        if (invalids == 0) {
            println "all cards are valid!"
        } else {
            int valid = cards.size() - invalids
            if (valid > 0) {
                println "plus $valid valid cards"
            }
        }
    }),

    COMPOSE({ CardSet cards, List<String> args ->
        if (args.size() < 1) {
            println "You must specify the renderset name to use (e.g., 'screen') after the descriptor."
            System.exit(2)
        }
        RenderSet rs = AssetDescriptor.fromStream(Main.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet(args.first())
        args = args.tail()

        File dir
        if (args.size() >= 1) {
            dir = new File(args.first())
            args = args.tail()
        } else {
            dir = new File('.')
        }
        if (dir.exists() && ! dir.directory) {
            println "You must specify a directory to compose into."
            System.exit(2)
        } else if (! dir.exists()) {
            dir.mkdirs()
        }

        println "Composing '$cards.name' into $dir:"
        int maxLen = cards*.title*.length().max() + cards.size().toString().length() + 5
        def validator = new CardValidator()
        def compositor = new AwtCompositor()
        cards.each { card ->
            println "#$card.cardOfSet $card.title".padRight(maxLen, '.')
            validator.validate(card).each {
                println "  " + it
            }
            compositor.compose(RenderModel.fromCard(card, rs), rs, new File(dir, "${card.cardOfSet}.png").newOutputStream())
        }
    }),

    final Closure action

    void execute(CardSet cards, List<String> args) {
        if (action.maximumNumberOfParameters == 1) {
            action(cards)
        } else {
            action(cards, args)
        }
    }

}