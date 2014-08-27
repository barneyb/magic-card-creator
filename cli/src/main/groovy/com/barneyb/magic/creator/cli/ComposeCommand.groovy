package com.barneyb.magic.creator.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "compose", commandDescription = "compose cards from a descriptor")
class ComposeCommand extends BaseDescriptorCommand {

    @Parameter(names = ["-o", "--output"], description = "The directory to compose into", required = true)
    File outputDir

    @Parameter(names = ["-p", "--print"], description = "Whether to rotate/bleed for printing")
    boolean forPrint = false

}
