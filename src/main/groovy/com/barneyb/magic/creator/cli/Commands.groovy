package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.asset.AssetDescriptor
import com.barneyb.magic.creator.asset.RenderSet
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
        if (args.size() < 1) {
            println "You must specify the target format to use (svg, png, or pdf) after the descriptor."
            System.exit(2)
        }
        Format format = Format.valueOf(args.first())
        args = args.tail()
        RenderSet rs = AssetDescriptor.fromStream(Main.classLoader.getResourceAsStream("assets/descriptor.json")).getRenderSet('print')

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
        def compositor = new SvgCompositor()
        cards.each { card ->
            println "#$card.cardOfSet $card.title".padRight(maxLen, '.')
            try {
                validator.validate(card).each {
                    println "  " + it
                }
                def baos = new ByteArrayOutputStream()
                compositor.compose(RenderModel.fromCard(card, rs), rs, baos)
                new File(dir, "${card.cardOfSet}.$format").bytes = format.formatter(baos.toByteArray())
            } catch (e) {
                e.printStackTrace()
            }
        }
        def proofs = new File(dir, "proofs.html")
        if (format.proofed) {
            proofs.text = """<html>
<head>
<style>
$format.proofTag { width: ${rs.frames.size.width}px; height: ${rs.frames.size.height}px; padding: 0; margin: 10px; }
</style>
</head>
<body>
${cards.collect { "<$format.proofTag src=\"${it.cardOfSet}.$format\" />" }.join("\n")}
</body>
</html>"""
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