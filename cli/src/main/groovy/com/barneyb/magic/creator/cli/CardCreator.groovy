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

    protected final MAIN_COMMAND = new MainCommand()
    protected final HELP_COMMAND = new HelpCommand()
    protected final SUB_COMMANDS = [
        validate: new ValidateCommand(),
        compose : new ComposeCommand(),
        stats   : new StatsCommand(),
        png     : new PngCommand(),
        pdf     : new PdfCommand(),
    ]

    JCommander jc

    CardCreator() {
        jc = new JCommander(MAIN_COMMAND)
        jc.addConverterFactory(new Converters())
        jc.programName = getClass().simpleName

        jc.addCommand(HELP_COMMAND)
        SUB_COMMANDS.each { n, c ->
            jc.addCommand(n, c)
        }
    }

    void run(String[] args) {
        jc.parse(args)
        def cn = jc.parsedCommand ?: 'help'
        def cmd = (cn == 'help') ? HELP_COMMAND : SUB_COMMANDS[cn]
        if (cmd instanceof Executable) {
            cmd.execute(MAIN_COMMAND, jc)
        } else {
            throw new NonExecutableCommandException(cmd)
        }
    }

    void help() {
        HELP_COMMAND.execute(MAIN_COMMAND, jc)
    }

    static void main(String[] args) {
        def cc = new CardCreator()
        try {
            cc.run(args)
        } catch (NonExecutableCommandException nece) {
            println nece.message
            cc.help()
            System.exit(3)
        } catch (MissingCommandException mce) {
            println mce.message
            cc.help()
            System.exit(2)
        } catch (ParameterException pe){
            println pe.message
            System.exit(1)
        }
    }

}
