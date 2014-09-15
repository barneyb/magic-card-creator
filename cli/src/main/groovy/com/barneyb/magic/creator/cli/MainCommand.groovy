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

    @Parameter(names = ["-h", "--help"], help = true)
    boolean help

    @Parameter(names = "--debug")
    boolean debug


}
