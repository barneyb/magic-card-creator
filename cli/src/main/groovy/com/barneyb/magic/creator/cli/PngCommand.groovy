package com.barneyb.magic.creator.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "png", commandDescription = "transcode composed cards to PNG (via docker)")
class PngCommand extends BaseTranscodeCommand {

    @Parameter(names = "--width", description = "The width the output images should be")
    int width = 400

    @Override
    String getUrlPath() {
        "/convert/png?w=$width"
    }

    @Override
    String getFileSuffix() {
        ".png"
    }

}
