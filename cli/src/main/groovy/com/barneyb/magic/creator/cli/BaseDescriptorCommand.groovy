package com.barneyb.magic.creator.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter

/**
 *
 *
 * @author barneyb
 */
abstract class BaseDescriptorCommand {

    @Parameter(names = ["-d", "--descriptor"], description = "Cardset descriptor", converter = FileConverter, required = true)
    File descriptor

}
