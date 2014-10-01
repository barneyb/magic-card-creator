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
        jc.addConverterFactory(new Converters())
        jc.programName = CardCreator.simpleName

        def commands = [
            help: new HelpCommand(),
            validate: new ValidateCommand(),
            compose: new ComposeCommand(),
            stats: new StatsCommand(),
            png: new PngCommand(),
            pdf: new PdfCommand(),
        ].each { n, c ->
            jc.addCommand(n, c)
        }

        try {
            jc.parse(args)
            def cmd = commands[jc.parsedCommand ?: "help"]
            if (cmd instanceof Executable) {
                cmd.execute(main, jc)
            } else {
                jc.usage()
            }
        } catch (MissingCommandException mce) {
            println mce.message
            commands.help.execute(main, jc)
            System.exit(2)
        } catch (ParameterException pe){
            println pe.message
            System.exit(1)
        }
    }

}
