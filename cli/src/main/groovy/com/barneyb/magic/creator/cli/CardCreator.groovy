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

        def commands = [
            help: new HelpCommand(),
            validate: new ValidateCommand(),
            compose: new ComposeCommand(),
            stats: new StatsCommand()
        ].each { n, c ->
            jc.addCommand(n, c)
        }

        try {
            jc.parse(args)
            def cmd = commands[jc.parsedCommand]
            if (cmd instanceof Executable) {
                cmd.execute(main)
            } else {
                jc.usage()
            }
        } catch (MissingCommandException mce) {
            println mce.message
            jc.usage()
        } catch (ParameterException pe){
            println pe.message
            jc.usage()
        }
    }

}
