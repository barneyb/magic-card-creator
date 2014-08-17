package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.PrintMorph
import com.barneyb.magic.creator.compositor.RenderModel
import com.barneyb.magic.creator.compositor.svg.SvgCompositor
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
            println "#$card.cardOfSet $card.title"
            try {
                def es = v.validate(card)
                if (es.size() > 0) {
                    invalids += 1
                    es.each {
                        println "  $it"
                    }
                }
            } catch (e) {
                invalids += 1
                e.printStackTrace()
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
        new CoreCompose().compose(cards, args)
    }),

    COMPOSE_PRINT({ CardSet cards, List<String> args ->
        new CoreCompose(true).compose(cards, args)
    })

    final Closure action

    void execute(CardSet cards, List<String> args) {
        if (action.maximumNumberOfParameters == 1) {
            action(cards)
        } else {
            action(cards, args)
        }
    }

    private static class CoreCompose {

        final boolean forPrint

        def CoreCompose(boolean forPrint=false) {
            this.forPrint = forPrint
        }

        void compose(CardSet cards, List<String> args) {
            RenderSet rs = AssetDescriptor.fromStream(Main.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet('print')

            File dir
            if (args.size() == 0) {
                println "You must specify the directory to compose into after the descriptor"
                System.exit(2)
            }
            dir = new File(args.first())
            if (dir.exists() && ! dir.directory) {
                println "You must specify a directory to compose into."
                System.exit(2)
            } else if (! dir.exists()) {
                dir.mkdirs()
            }

            println "Composing '$cards.name' into $dir:"
            int maxLen = cards*.title*.length().max() + cards.size().toString().length() + 5
            def validator = new CardValidator()
            def compositor = new SvgCompositor(printMorph: forPrint ? new PrintMorph(17.5f, 17.5f, 90) : null)
            cards.each { card ->
                println "#$card.cardOfSet $card.title".padRight(maxLen, '.')
                try {
                    validator.validate(card).each {
                        println "  " + it
                    }
                    compositor.compose(
                        RenderModel.fromCard(card, rs),
                        rs,
                        new File(dir, "${card.cardOfSet}.svg").newOutputStream()
                    )
                } catch (e) {
                    e.printStackTrace()
                }
            }

            new File(dir, "proofs.html").text = """<!DOCTYPE html>
<html>
<body>
${cards.collect { "<embed src=\"${it.cardOfSet}.svg\" />" }.join("\n")}
</body>
</html>"""
        }
    }

}