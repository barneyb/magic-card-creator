package com.barneyb.magic.creator.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.MissingCommandException
import com.beust.jcommander.ParameterException

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
            switch (jc.parsedCommand) {
                case "validate":
                    validate.execute()
                    break
                case "compose":
                    compose.execute()
                    break
                case null:
                case "help":
                    jc.usage()
                    break
            }
        } catch (MissingCommandException mce) {
            println mce.message
            jc.usage()
        } catch (ParameterException pe){
            println pe.message
            if (jc.parsedCommand) {
                jc.usage(jc.parsedCommand)
            } else {
                jc.usage()
            }
        }
    }

}
