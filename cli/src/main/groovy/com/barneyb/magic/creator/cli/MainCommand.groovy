package com.barneyb.magic.creator.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(separators = "=")
class MainCommand {

    @Parameter(names = ["-h", "--help"], help = true, description = "display usage information and exit")
    boolean help

    @Parameter(names = "--debug", description = "enable debug mode")
    boolean debug

}
