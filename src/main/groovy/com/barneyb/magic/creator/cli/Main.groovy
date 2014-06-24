package com.barneyb.magic.creator.cli
import com.barneyb.magic.creator.descriptor.CardSet
import com.barneyb.magic.creator.descriptor.CardValidator
import com.barneyb.magic.creator.descriptor.MarkdownDescriptor
import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
class Main {

    @TupleConstructor
    static enum Commands {

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
            println "Validating '$cards.name'"
            def v = new CardValidator()
            int invalids = 0
            cards.each { card ->
                def es = v.validate(card)
                if (es.size() > 0) {
                    invalids += 1
                    println card.title
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
        })

        final Closure action

        void execute(CardSet cards, List<String> args) {
            if (action.maximumNumberOfParameters == 1) {
                action(cards)
            } else {
                action(cards, args)
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].toLowerCase() == "help") {
            Commands.HELP.action()
        }
        if (args.length < 2) {
            Commands.HELP.action "You must supply a command and a descriptor file as the first two arguments."
        }
        def command = args[0].toUpperCase()
        if (! Commands.enumConstants*.name().contains(command)) {
            Commands.HELP.action "The '$command' command is not recognized (${Commands.enumConstants*.name()*.toLowerCase()})."
        }
        def cmd = Commands.valueOf(command)
        def desc = new File(args[1])
        if (! desc.name.endsWith(".md")) {
            Commands.HELP.action "Descriptors must be Markdown files with an 'md' extension."
        }
        if (! desc.exists()) {
            Commands.HELP.action "The specified descriptor file doesn't exit."
        }
        cmd.execute(new MarkdownDescriptor(desc.toURI().toURL()).cardSet, args[2..<args.length])
    }

}
