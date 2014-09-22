package com.barneyb.magic.creator.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "help", commandDescription = "display usage information and exit")
class HelpCommand implements Executable {

    @Parameter(description = "command to get help for")
    List<String> commands

    @Override
    void execute(MainCommand main, JCommander jc) {
        if (commands == null || commands.empty) {
            println "Usage: $CardCreator.simpleName [options] [command] [command options]"
            println()
            println "A toolkit for creating custom Magic: The Gathering cards."
            println()
            println "Commands:"
            def maxlen = jc.commands.keySet()*.length().max()
            jc.commands.each { n, c ->
                println "    ${n.padRight(maxlen)}  ${jc.getCommandDescription(n)}"
            }
        } else {
            commands.each {
                jc.usage(it)
            }
        }
    }

}
