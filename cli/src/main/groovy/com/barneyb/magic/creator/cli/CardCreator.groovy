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

    public static final String HELP_COMMAND_NAME = 'help'

    protected final MAIN_COMMAND = new MainCommand()
    protected final HELP_COMMAND = new HelpCommand()
    protected final SUB_COMMANDS = [
        validate: new ValidateCommand(),
        compose : new ComposeCommand(),
        stats   : new StatsCommand(),
        png     : new PngCommand(),
        pdf     : new PdfCommand(),
        md2xml  : new MarkdownToXmlCommand(),
        icons   : new SymbolIconCommand()
    ]

    JCommander jc

    CardCreator() {
        jc = new JCommander(MAIN_COMMAND)
        jc.addConverterFactory(new Converters())
        jc.programName = getClass().simpleName

        jc.addCommand(HELP_COMMAND_NAME, HELP_COMMAND)
        SUB_COMMANDS.each { n, c ->
            jc.addCommand(n, c)
        }
    }

    void run(String[] args) {
        jc.parse(args)
        def cn = jc.parsedCommand ?: HELP_COMMAND_NAME
        def cmd = (cn == HELP_COMMAND_NAME) ? HELP_COMMAND : SUB_COMMANDS[cn]
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
