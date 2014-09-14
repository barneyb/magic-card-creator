package com.barneyb.magic.creator.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.MissingCommandException
/**
 *
 *
 * @author barneyb
 */
class CardCreator {

    static void main(String[] args) {
        def main = new MainCommand()
        def jc = new JCommander(main)
        jc.programName = CardCreator.simpleName

        def help = new HelpCommand()
        jc.addCommand("help", help)

        def validate = new ValidateCommand()
        jc.addCommand("validate", validate)

        def compose = new ComposeCommand()
        jc.addCommand("compose", compose)

        try {
            jc.parse(args)
        } catch (MissingCommandException mce) {
            println mce.message
        }
        switch (jc.parsedCommand) {
            case "validate":
                validate.execute()
                break
            case "compose":
                // todo: implement composition
                break
            case null:
            case "help":
                jc.usage()
                break
        }
    }

}
